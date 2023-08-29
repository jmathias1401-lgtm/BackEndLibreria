package newfarma.productos;
import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/producto")
public class ProductoController {
    ProductoServiceI serviceI;
    ProductoRepositoryJPA productoRepositoryJPA;
    public ProductoController(ProductoServiceI serviceI,ProductoRepositoryJPA productoRepositoryJPA){
        this.serviceI=serviceI;
        this.productoRepositoryJPA=productoRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<ProductListResponse>list(ProductListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Producto producto )
    {
        return new ResponseEntity(serviceI.save(producto), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}
