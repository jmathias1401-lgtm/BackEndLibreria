package newfarma.kardex;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.kardex.dto.KardexListResponse;
import newfarma.kardex.dto.KardexRequestDto;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/kardex")
@Tag(name = "Kardex", description = "Control y trazabilidad de movimientos de inventario en base de datos")
public class KardexController {
    private final KardexServiceI kardexService;

    public KardexController(KardexServiceI kardexService) {
        this.kardexService = kardexService;
    }

    @Operation(summary = "Listar movimientos de kardex por producto", description = "Obtiene el historial de cambios y movimientos de inventario de un producto")
    @GetMapping("/producto/{id}")
    public ResponseEntity<KardexListResponse> listByProducto(@PathVariable Long id) {
        return new ResponseEntity<>(kardexService.listByProducto(id), HttpStatus.OK);
    }

    @Operation(summary = "Registrar movimiento de kardex", description = "Guarda un movimiento o ajuste de inventario en la base de datos MySQL")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody KardexRequestDto dto) {
        return new ResponseEntity<>(kardexService.save(dto), HttpStatus.OK);
    }
}

