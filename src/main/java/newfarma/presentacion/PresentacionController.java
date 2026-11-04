package newfarma.presentacion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.Presentacion;
import newfarma.presentacion.dto.PresentacionListRequest;
import newfarma.presentacion.dto.PresentacionListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/presentacion")
@Tag(name = "Presentaciones", description = "Formatos de presentación de productos (caja, blíster, frasco, etc.)")
public class PresentacionController {
    private final PresentacionServiceI serviceI;

    public PresentacionController(PresentacionServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar presentaciones", description = "Obtiene la lista paginada de presentaciones disponibles")
    @GetMapping
    public ResponseEntity<PresentacionListResponse> list(PresentacionListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar presentación", description = "Crea o actualiza una presentación de producto")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Presentacion presentacion) {
        return new ResponseEntity<>(serviceI.save(presentacion), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar presentación", description = "Elimina una presentación por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único de la presentación", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
