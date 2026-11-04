package newfarma.empleado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.empleado.dto.EmpleadoListResponse;
import newfarma.model.Empleado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/empleado")
@Tag(name = "Empleados", description = "Gestión del personal y colaboradores de la empresa")
public class EmpleadoController {
    private final EmpleadoServiceI serviceI;

    public EmpleadoController(EmpleadoServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar empleados", description = "Obtiene un listado paginado de empleados registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleados listados con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoListResponse.class)))
    })
    @GetMapping
    public ResponseEntity<EmpleadoListResponse> list(EmpleadoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar empleado", description = "Crea o actualiza la ficha de un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado procesado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de empleado inválidos")
    })
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Empleado empleado) {
        return new ResponseEntity<>(serviceI.save(empleado), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado eliminado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del empleado", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
