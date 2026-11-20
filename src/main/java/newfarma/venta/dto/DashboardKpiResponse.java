package newfarma.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardKpiResponse {
    private Double ventasHoyMonto;
    private Integer ventasHoyCantidad;
    private Double ticketPromedioHoy;
    private Double ventasMesMonto;
    private Integer ventasMesCantidad;
    private Integer productosStockCritico;
    private Integer productosAgotados;
    private Integer totalProductos;
    private List<VentasHoraDto> ventasPorHoraHoy;
    private List<VentasComprobanteDto> ventasPorComprobanteMes;
    private List<VendedorRendimientoDto> topVendedoresMes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VentasHoraDto {
        private Integer hora;
        private Integer cantidad;
        private Double total;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VentasComprobanteDto {
        private String tipoComprobante;
        private Integer cantidad;
        private Double total;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VendedorRendimientoDto {
        private Long idEmpleado;
        private String vendedor;
        private Integer cantidadVentas;
        private Double totalMonto;
    }
}
