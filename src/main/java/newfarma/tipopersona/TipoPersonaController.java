package newfarma.tipopersona;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.tipopersona.dto.TipoPersonaListRequest;
import newfarma.tipopersona.dto.TipoPersonaListResponse;
import newfarma.model.TipoPersona;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tipopersona")
@Tag(name = "Tipos de Persona", description = "Clasificación de personas (Natural, Jurídica, etc.)")
public class TipoPersonaController {
    private final TipoPersonaServiceI serviceI;

    public TipoPersonaController(TipoPersonaServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar tipos de persona", description = "Obtiene los tipos de persona registrados")
    @GetMapping
    public ResponseEntity<TipoPersonaListResponse> list(TipoPersonaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar tipo de persona", description = "Crea o modifica un tipo de persona")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody TipoPersona tipopersona) {
        return new ResponseEntity<>(serviceI.save(tipopersona), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar tipo de persona", description = "Elimina un tipo de persona por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del tipo de persona", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
