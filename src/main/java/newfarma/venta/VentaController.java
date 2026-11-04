package newfarma.venta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import newfarma.utils.BaseResponse;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.venta.dto.VentaRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/venta")
@Tag(name = "Ventas", description = "Gestión de transacciones comerciales de venta, comprobantes y reportes")
public class VentaController {
    private final VentaServiceI serviceI;
    private final VentaRepositoryJPA personaRepositoryJPA;

    public VentaController(VentaServiceI serviceI, VentaRepositoryJPA personaRepositoryJPA) {
        this.serviceI = serviceI;
        this.personaRepositoryJPA = personaRepositoryJPA;
    }

    @Operation(summary = "Listar ventas", description = "Obtiene una lista paginada y filtrada de ventas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VentaListResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<VentaListResponse> list(VentaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar venta", description = "Crea una nueva venta o actualiza una existente a partir de un DTO validado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta procesada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de venta inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno al registrar la venta")
    })
    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody VentaRequestDto ventaDto) {
        return new ResponseEntity<>(serviceI.save(ventaDto), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar venta", description = "Elimina una venta del sistema por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta eliminada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único de la venta", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
