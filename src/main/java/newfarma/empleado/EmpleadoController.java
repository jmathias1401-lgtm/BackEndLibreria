package newfarma.empleado;

import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.empleado.dto.EmpleadoListResponse;
import newfarma.model.Empleado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/empleado")
public class EmpleadoController {
    private final EmpleadoServiceI serviceI;

    public EmpleadoController(EmpleadoServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @GetMapping
    public ResponseEntity<EmpleadoListResponse> list(EmpleadoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Empleado empleado) {
        return new ResponseEntity<>(serviceI.save(empleado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
