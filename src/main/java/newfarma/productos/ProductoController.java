package newfarma.productos;

import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.utils.BaseResponse;

import org.springframework.http.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/producto")
public class ProductoController {
    private final ProductoServiceI serviceI;
    private final ProductoRepositoryJPA productoRepositoryJPA;

    public ProductoController(ProductoServiceI serviceI, ProductoRepositoryJPA productoRepositoryJPA) {
        this.serviceI = serviceI;
        this.productoRepositoryJPA = productoRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<ProductListResponse> list(ProductListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductListResponse> listbyId(@PathVariable int id) {
        return new ResponseEntity<>(serviceI.ProductListById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> save(
            @RequestPart("producto") Producto producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return new ResponseEntity<>(serviceI.save(producto, imagen), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
