package newfarma.venta;

import jakarta.validation.Valid;
import newfarma.model.Persona;
import newfarma.model.Venta;
import newfarma.venta.VentaRepositoryJPA;
import newfarma.venta.VentaServiceI;
import newfarma.utils.BaseResponse;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.venta.dto.VentaRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/venta")
public class VentaController {
    private final VentaServiceI serviceI;
    private final VentaRepositoryJPA personaRepositoryJPA;

    public VentaController(VentaServiceI serviceI, VentaRepositoryJPA personaRepositoryJPA) {
        this.serviceI = serviceI;
        this.personaRepositoryJPA = personaRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<VentaListResponse> list(VentaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody VentaRequestDto ventaDto) {
        return new ResponseEntity<>(serviceI.save(ventaDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
