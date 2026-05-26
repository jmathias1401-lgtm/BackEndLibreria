package newfarma.laboratorio;

import newfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.model.Laboratorio;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/laboratorio")
public class LaboratorioController {
    private final LaboratorioServiceI serviceI;

    public LaboratorioController(LaboratorioServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @GetMapping
    public ResponseEntity<LaboratorioListResponse> list(LaboratorioListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Laboratorio laboratorio) {
        return new ResponseEntity<>(serviceI.save(laboratorio), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
