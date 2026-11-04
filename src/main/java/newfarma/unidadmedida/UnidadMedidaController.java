package newfarma.unidadmedida;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.UnidadMedida;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/unidadmedida")
@Tag(name = "Unidades de Medida", description = "Catálogo de unidades de medida para productos (mg, ml, unidades, etc.)")
public class UnidadMedidaController {
    private final UnidadMedidaServiceI serviceI;

    public UnidadMedidaController(UnidadMedidaServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar unidades de medida", description = "Obtiene una lista paginada de unidades de medida")
    @GetMapping
    public ResponseEntity<UnidadMedidaListResponse> list(UnidadMedidaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar unidad de medida", description = "Crea o actualiza una unidad de medida")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody UnidadMedida unidadMedida) {
        return new ResponseEntity<>(serviceI.save(unidadMedida), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar unidad de medida", description = "Elimina una unidad de medida por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador de la unidad de medida", required = true, example = "1")
            @PathVariable Integer id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
