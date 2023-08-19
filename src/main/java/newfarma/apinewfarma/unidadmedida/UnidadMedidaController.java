package newfarma.apinewfarma.unidadmedida;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.apinewfarma.productos.ProductoRepositoryJPA;
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
}
