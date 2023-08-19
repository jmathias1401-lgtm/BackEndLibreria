package newfarma.apinewfarma.productos;

import newfarma.apinewfarma.productos.dto.ProductListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("Api/productos")
public class ProductoController {

    ProductoServiceI serviceI;
    ProductoRepositoryJPA productoRepositoryJPA;
    public ProductoController(ProductoServiceI serviceI,ProductoRepositoryJPA productoRepositoryJPA){
        this.serviceI=serviceI;
        this.productoRepositoryJPA=productoRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<ProductListResponse>list(String codigo){
        return new ResponseEntity(serviceI.list(codigo), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
