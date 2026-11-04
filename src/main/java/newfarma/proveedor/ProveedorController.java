package newfarma.proveedor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.Proveedor;
import newfarma.proveedor.dto.ProveedorListRequest;
import newfarma.proveedor.dto.ProveedorResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/proveedor")
@Tag(name = "Proveedores", description = "Gestión de proveedores comerciales y distribuidores")
public class ProveedorController {
    private final ProveedorServiceI serviceI;

    public ProveedorController(ProveedorServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar proveedores", description = "Obtiene un listado paginado de proveedores registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedores listados con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProveedorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ProveedorResponse> list(ProveedorListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar proveedor", description = "Crea o actualiza la ficha de un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor procesado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de proveedor inválidos")
    })
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Proveedor proveedor) {
        return new ResponseEntity<>(serviceI.save(proveedor), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor eliminado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del proveedor", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
