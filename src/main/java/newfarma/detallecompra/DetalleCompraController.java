package newfarma.detallecompra;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.detallecompra;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.detallecompra.dto.DetalleCompraListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/detallecompra")
@Tag(name = "Detalle Compra", description = "Líneas de detalle e ítems adquiridos en órdenes de compra")
public class DetalleCompraController {
    private final DetalleCompraServiceI serviceI;
    private final DetalleCompraRepositoryJPA detalleCompraRepositoryJPA;

    public DetalleCompraController(DetalleCompraServiceI serviceI, DetalleCompraRepositoryJPA detalleCompraRepositoryJPA) {
        this.serviceI = serviceI;
        this.detalleCompraRepositoryJPA = detalleCompraRepositoryJPA;
    }

    @Operation(summary = "Listar detalles de compra", description = "Obtiene los detalles de compra según los filtros provistos")
    @GetMapping
    public ResponseEntity<DetalleCompraListResponse> list(DetalleCompraListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Consultar detalle de compra por ID", description = "Obtiene una línea de detalle de compra por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompraListResponse> listbyId(@PathVariable int id) {
        return new ResponseEntity<>(serviceI.DetalleCompraListById(id), HttpStatus.OK);
    }

    @Operation(summary = "Consultar detalles de compra por ID de producto", description = "Obtiene los registros de compras históricas de un producto específico")
    @GetMapping("/producto/{id}")
    public ResponseEntity<DetalleCompraListResponse> listbyproductoId(@PathVariable int id) {
        return new ResponseEntity<>(serviceI.listDetalleCompraByProductoId(id), HttpStatus.OK);
    }

    @Operation(summary = "Guardar detalle de compra", description = "Registra una línea de detalle para una orden de compra")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody detallecompra detalleCompra) {
        return new ResponseEntity<>(serviceI.save(detalleCompra), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar detalle de compra", description = "Elimina una línea de detalle de compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
