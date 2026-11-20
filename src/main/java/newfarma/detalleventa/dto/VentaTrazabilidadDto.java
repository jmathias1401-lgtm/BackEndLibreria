package newfarma.detalleventa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaTrazabilidadDto {
    private Long idDetalleVenta;
    private String fechaVenta;
    private String comprobante;
    private Integer unidades;
    private Double precioUnitario;
    private Double total;
    private String vendedor;
    private String cliente;
}
