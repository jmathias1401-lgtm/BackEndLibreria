package newfarma.productos;
import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//@CrossOrigin(origins = "*")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/producto")
//@CrossOrigin(origins = {"http:localhost:4200","http:localhost:8000"})
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
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductListResponse>listbyId(@PathVariable int id){
        return new ResponseEntity(serviceI.ProductListById(id), HttpStatus.OK);
    }


    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Producto producto )
    {
        //return new ResponseEntity("respuesta desde SAVE_PRODUCTO", HttpStatus.OK);
        return new ResponseEntity(serviceI.save(producto), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }


}
