package newfarma.detalleventa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.detalleventa;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/detalleventa")
@Tag(name = "Detalle Venta", description = "Líneas de detalle, ítems vendidos y estadísticas de ventas por período")
public class DetalleVentaController {
    private final DetalleVentaServiceI serviceI;
    private final DetalleVentaRepositoryJPA detalleVentaRepositoryJPA;

    public DetalleVentaController(DetalleVentaServiceI serviceI, DetalleVentaRepositoryJPA detalleVentaRepositoryJPA) {
        this.serviceI = serviceI;
        this.detalleVentaRepositoryJPA = detalleVentaRepositoryJPA;
    }

    @Operation(summary = "Listar detalles de ventas", description = "Obtiene los detalles de venta según los filtros provistos")
    @GetMapping
    public ResponseEntity<DetalleVentaListResponse> list(DetalleVentaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Consultar detalle de venta por ID", description = "Obtiene un ítem de detalle de venta por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaListResponse> listbyId(@PathVariable int id) {
        return new ResponseEntity<>(serviceI.DetalleVentaListById(id), HttpStatus.OK);
    }

    @Operation(summary = "Guardar detalle de venta", description = "Registra una línea de detalle para una venta")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody detalleventa detalleVenta) {
        return new ResponseEntity<>(serviceI.save(detalleVenta), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar detalle de venta", description = "Elimina una línea de detalle de venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }

    @Operation(summary = "Consultar ventas por mes y año", description = "Obtiene el consolidado de detalles de ventas filtrado por mes y año")
    @GetMapping("ventasMes/{mes}/{anio}")
    public ResponseEntity<DetalleVentaListResponse> ventasMes(@PathVariable int mes, @PathVariable int anio) {
        return new ResponseEntity<>(serviceI.ventasMes(mes, anio), HttpStatus.OK);
    }
}
