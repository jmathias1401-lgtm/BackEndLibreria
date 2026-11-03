package newfarma.cargo;

import jakarta.validation.Valid;
import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.cargo.dto.CargoRequestDto;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cargo")
public class CargoController {
    private final CargoServiceI serviceI;
    private final CargoRepositoryJPA cargoRepositoryJPA;

    public CargoController(CargoServiceI serviceI, CargoRepositoryJPA cargoRepositoryJPA) {
        this.serviceI = serviceI;
        this.cargoRepositoryJPA = cargoRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<CargoListResponse> list(CargoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody CargoRequestDto cargoDto) {
        return new ResponseEntity<>(serviceI.save(cargoDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
