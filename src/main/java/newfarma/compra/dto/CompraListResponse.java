package newfarma.compra.dto;

import lombok.*;
import newfarma.model.Compra;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class CompraListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Compra> list;
}