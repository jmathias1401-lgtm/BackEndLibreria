package newfarma.compra;

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
public class CompraController {
    private final CompraServiceI serviceI;
    private final CompraRepositoryJPA compraRepositoryJPA;

    public CompraController(CompraServiceI serviceI, CompraRepositoryJPA compraRepositoryJPA) {
        this.serviceI = serviceI;
        this.compraRepositoryJPA = compraRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<CompraListResponse> list(CompraListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody CompraRequestDto compraDto) {
        return new ResponseEntity<>(serviceI.save(compraDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
