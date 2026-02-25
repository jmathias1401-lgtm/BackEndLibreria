package newfarma.detallecompra;
import newfarma.model.detallecompra;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.detallecompra.dto.DetalleCompraListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/detallecompra")
public class DetalleCompraController {
    DetalleCompraServiceI serviceI;
    DetalleCompraRepositoryJPA detalleCompraRepositoryJPA;
    public DetalleCompraController(DetalleCompraServiceI serviceI,DetalleCompraRepositoryJPA detalleCompraRepositoryJPA){
        this.serviceI=serviceI;
        this.detalleCompraRepositoryJPA=detalleCompraRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<DetalleCompraListResponse>list(DetalleCompraListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalleCompraListResponse>listbyId(@PathVariable int id){
        return new ResponseEntity(serviceI.DetalleCompraListById(id), HttpStatus.OK);
    }

    @GetMapping("/producto/{id}")
    @ResponseBody
    public ResponseEntity<DetalleCompraListResponse>listbyproductoId(@PathVariable int id){
        return new ResponseEntity<>(serviceI.listDetalleCompraByProductoId(id),HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody detallecompra detalleCompra )
    {
        return new ResponseEntity(serviceI.save(detalleCompra), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}