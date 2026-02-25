package newfarma.detalleventa;
import newfarma.model.detalleventa;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/detalleventa")
public class DetalleVentaController {
    DetalleVentaServiceI serviceI;
    DetalleVentaRepositoryJPA detalleVentaRepositoryJPA;
    public DetalleVentaController(DetalleVentaServiceI serviceI,DetalleVentaRepositoryJPA detalleVentaRepositoryJPA){
        this.serviceI=serviceI;
        this.detalleVentaRepositoryJPA=detalleVentaRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<DetalleVentaListResponse>list(DetalleVentaListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalleVentaListResponse>listbyId(@PathVariable int id){
        return new ResponseEntity(serviceI.DetalleVentaListById(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody detalleventa detalleVenta )
    {
        return new ResponseEntity(serviceI.save(detalleVenta), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}