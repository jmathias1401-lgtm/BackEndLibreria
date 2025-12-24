package newfarma.detalleventa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaDTO {
    private Long iddetalleventa;
    private Long venta_idventa;
    private Long producto_idproducto;
    private String codigodetalleventa;
    private Integer unidades;
    private Double costounidad;
    private Double subtotal;
    private Double descuentounidad;
    private Double total;
}