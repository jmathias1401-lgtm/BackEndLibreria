package newfarma.detallecompra.dto;

import lombok.*;
import newfarma.model.DetalleCompra;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class DetalleCompraListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private Double lastCost;
    private List<DetalleCompra> list;
}