package newfarma.proveedor;

import newfarma.proveedor.dto.ProveedorListRequest;
import newfarma.proveedor.dto.ProveedorResponse;
import newfarma.model.Proveedor;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/proveedor")
public class ProveedorController {
    private final ProveedorServiceI serviceI;

    public ProveedorController(ProveedorServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @GetMapping
    public ResponseEntity<ProveedorResponse> list(ProveedorListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Proveedor proveedor) {
        return new ResponseEntity<>(serviceI.save(proveedor), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
