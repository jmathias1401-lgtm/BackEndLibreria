package newfarma.cargo;

import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.model.Cargo;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cargo")
public class CargoController {
    private final CargoServiceI serviceI;

    public CargoController(CargoServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @GetMapping
    public ResponseEntity<CargoListResponse> list(CargoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Cargo cargo) {
        return new ResponseEntity<>(serviceI.save(cargo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
