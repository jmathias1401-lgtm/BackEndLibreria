package newfarma.apinewfarma.presentacion;
import newfarma.apinewfarma.presentacion.dto.PresentacionListRequest;
import newfarma.apinewfarma.presentacion.dto.PresentacionListResponse;
import newfarma.apinewfarma.productos.ProductoRepositoryJPA;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/presentacion")
public class PresentacionController {
    PresentacionServiceI serviceI;
    public PresentacionController(PresentacionServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<PresentacionListResponse>list(PresentacionListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
