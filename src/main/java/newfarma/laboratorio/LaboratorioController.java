package newfarma.laboratorio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.model.Laboratorio;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/laboratorio")
@Tag(name = "Laboratorios", description = "Gestión de laboratorios fabricantes de productos farmacéuticos")
public class LaboratorioController {
    private final LaboratorioServiceI serviceI;

    public LaboratorioController(LaboratorioServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar laboratorios", description = "Obtiene una lista paginada de laboratorios")
    @GetMapping
    public ResponseEntity<LaboratorioListResponse> list(LaboratorioListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar laboratorio", description = "Crea o actualiza los datos de un laboratorio fabricante")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Laboratorio laboratorio) {
        return new ResponseEntity<>(serviceI.save(laboratorio), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar laboratorio", description = "Elimina un laboratorio por su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del laboratorio", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
