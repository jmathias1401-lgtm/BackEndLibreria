package newfarma.detalleventa.dto;

import lombok.*;
import newfarma.model.detalleventa;

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
    private List<detalleventa> list;
}