package newfarma.empleado;

import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.empleado.dto.EmpleadoListResponse;
import newfarma.model.Empleado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/empleado")
class EmpleadoController {
    EmpleadoServiceI serviceI;

    public EmpleadoController(EmpleadoServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<EmpleadoListResponse> list(EmpleadoListRequest params) {
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Empleado empleado) {
        return new ResponseEntity(serviceI.save(empleado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity(serviceI.eliminar(id), HttpStatus.OK);
    }
}