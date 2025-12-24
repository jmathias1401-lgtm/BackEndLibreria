package newfarma.detalleventa.dto;

import lombok.*;
import newfarma.model.DetalleVenta;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class DetalleVentaListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<DetalleVenta> list;
}