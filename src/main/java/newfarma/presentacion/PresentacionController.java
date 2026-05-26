package newfarma.presentacion;

import newfarma.model.Presentacion;
import newfarma.presentacion.dto.PresentacionListRequest;
import newfarma.presentacion.dto.PresentacionListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/presentacion")
public class PresentacionController {
    private final PresentacionServiceI serviceI;

    public PresentacionController(PresentacionServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @GetMapping
    public ResponseEntity<PresentacionListResponse> list(PresentacionListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Presentacion presentacion) {
        return new ResponseEntity<>(serviceI.save(presentacion), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
