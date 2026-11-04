package newfarma.pedido;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.Pedido;
import newfarma.pedido.dto.PedidoListRequest;
import newfarma.pedido.dto.PedidoListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/pedido")
@Tag(name = "Pedidos", description = "Gestión de pedidos de clientes y generación de correlativos")
public class PedidoController {
    private final PedidoServiceI serviceI;

    public PedidoController(PedidoServiceI serviceI) {
        this.serviceI = serviceI;
    }

    @Operation(summary = "Listar pedidos", description = "Obtiene una lista paginada y filtrada de pedidos")
    @GetMapping
    public ResponseEntity<PedidoListResponse> list(PedidoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar pedido", description = "Crea un nuevo pedido o actualiza uno existente")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Pedido pedido) {
        return new ResponseEntity<>(serviceI.save(pedido), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del pedido", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener último código de pedido", description = "Retorna el último correlativo generado para correlativos secuenciales")
    @GetMapping("/lastPedido")
    public ResponseEntity<Map<String, String>> lastPedido() throws Exception {
        String dato = serviceI.lastPedido();
        Map<String, String> response = Map.of("codigopedido", dato);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
