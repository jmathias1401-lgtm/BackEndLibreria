package newfarma.venta.dto;

import lombok.*;
import newfarma.model.Venta;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class VentaListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Venta> list;
}
