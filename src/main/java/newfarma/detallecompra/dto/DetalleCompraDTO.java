package newfarma.detallecompra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompraDTO {
    private Long iddetallecompra;
    private Long compra_idcompra;
    private Long producto_idproducto;
    private String codigodetallecompra;
    private Integer unidades;
    private Double costounidad;
    private Double subtotal;
    private Double descuentounidad;
    private Double total;
}