package newfarma.empresa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.empresa;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.empresa.dto.EmpresaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/empresa")
@Tag(name = "Empresas", description = "Datos de configuración institucional de la empresa y sucursal")
public class EmpresaController {
    private final EmpresaServiceI serviceI;
    private final EmpresaRepositoryJPA empresaRepositoryJPA;

    public EmpresaController(EmpresaServiceI serviceI, EmpresaRepositoryJPA empresaRepositoryJPA) {
        this.serviceI = serviceI;
        this.empresaRepositoryJPA = empresaRepositoryJPA;
    }

    @Operation(summary = "Listar empresas", description = "Obtiene los datos de la empresa o sucursales")
    @GetMapping
    public ResponseEntity<EmpresaListResponse> list(EmpresaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar empresa", description = "Crea o modifica los datos institucionales de la empresa")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody empresa empresa) {
        return new ResponseEntity<>(serviceI.save(empresa), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar empresa", description = "Elimina un registro de empresa")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único de la empresa", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
