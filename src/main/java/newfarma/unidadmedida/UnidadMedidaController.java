package newfarma.unidadmedida;
import newfarma.model.UnidadMedida;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.productos.ProductoRepositoryJPA;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/unidadmedida")
public class UnidadMedidaController {
    UnidadMedidaServiceI serviceI;
    ProductoRepositoryJPA productoRepositoryJPA;
    public UnidadMedidaController(UnidadMedidaServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<UnidadMedidaListResponse>list(UnidadMedidaListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody UnidadMedida unidadMedida )
    {
        return new ResponseEntity(serviceI.save(unidadMedida), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}
