package newfarma.estado;
import newfarma.estado.dto.EstadoListRequest;
import newfarma.estado.dto.EstadoListResponse;
import newfarma.model.Estado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/estado")
class EstadoController {
    EstadoServiceI serviceI;

    public EstadoController(EstadoServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<EstadoListResponse>list(EstadoListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Estado estado )
    {
        return new ResponseEntity(serviceI.save(estado), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}