package newfarma.empresa;

import newfarma.model.empresa;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.empresa.dto.EmpresaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/empresa")
public class EmpresaController {
    private final EmpresaServiceI serviceI;
    private final EmpresaRepositoryJPA empresaRepositoryJPA;

    public EmpresaController(EmpresaServiceI serviceI, EmpresaRepositoryJPA empresaRepositoryJPA) {
        this.serviceI = serviceI;
        this.empresaRepositoryJPA = empresaRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<EmpresaListResponse> list(EmpresaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody empresa empresa) {
        return new ResponseEntity<>(serviceI.save(empresa), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
