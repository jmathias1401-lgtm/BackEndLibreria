package newfarma.compra;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import newfarma.compra.dto.CompraListRequest;
import newfarma.compra.dto.CompraListResponse;
import newfarma.compra.dto.CompraRequestDto;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/compra")
@Tag(name = "Compras", description = "Gestión de adquisiciones a proveedores, registros de compras y comprobantes")
public class CompraController {
    private final CompraServiceI serviceI;
    private final CompraRepositoryJPA compraRepositoryJPA;

    public CompraController(CompraServiceI serviceI, CompraRepositoryJPA compraRepositoryJPA) {
        this.serviceI = serviceI;
        this.compraRepositoryJPA = compraRepositoryJPA;
    }

    @Operation(summary = "Listar compras", description = "Obtiene una lista paginada y filtrada de compras registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de compras obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompraListResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<CompraListResponse> list(CompraListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar compra", description = "Crea una nueva compra a proveedor o actualiza una existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra procesada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de compra inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno o correlativo duplicado")
    })
    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody CompraRequestDto compraDto) {
        return new ResponseEntity<>(serviceI.save(compraDto), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar compra", description = "Elimina una compra del sistema por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra eliminada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único de la compra", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
