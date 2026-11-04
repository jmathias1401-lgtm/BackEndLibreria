package newfarma.estado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.estado.dto.EstadoListRequest;
import newfarma.estado.dto.EstadoListResponse;
import newfarma.model.Estado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/estado")
@Tag(name = "Estados", description = "Catálogo de estados operativos de entidades (Activo, Inactivo, etc.)")
public class EstadoController {
    private final EstadoServiceI serviceI;

    public EstadoController(EstadoServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar estados", description = "Obtiene la lista de estados del sistema")
    @GetMapping
    public ResponseEntity<EstadoListResponse> list(EstadoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar estado", description = "Crea o actualiza un estado")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Estado estado) {
        return new ResponseEntity<>(serviceI.save(estado), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar estado", description = "Elimina un estado por su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del estado", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
