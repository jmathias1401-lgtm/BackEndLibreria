package newfarma.compra;
import newfarma.model.Compra;
import newfarma.compra.CompraRepositoryJPA;
import newfarma.compra.CompraServiceI;
import newfarma.utils.BaseResponse;
import newfarma.compra.dto.CompraListRequest;
import newfarma.compra.dto.CompraListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/compra")
public class CompraController {
    CompraServiceI serviceI;
    CompraRepositoryJPA compraRepositoryJPA;
    public CompraController(CompraServiceI serviceI, CompraRepositoryJPA compraRepositoryJPA){
        this.serviceI=serviceI;
        this.compraRepositoryJPA=compraRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<CompraListResponse>list(CompraListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Compra compra )
    {
        return new ResponseEntity(serviceI.save(compra), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}