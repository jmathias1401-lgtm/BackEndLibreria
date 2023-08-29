package newfarma.cargo;
import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.model.Cargo;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/cargo")
class CargoController {
    CargoServiceI serviceI;

    public CargoController(CargoServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<CargoListResponse>list(CargoListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Cargo cargo )
    {
        return new ResponseEntity(serviceI.save(cargo), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}
