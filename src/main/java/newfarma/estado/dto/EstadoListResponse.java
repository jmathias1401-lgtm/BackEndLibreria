package newfarma.estado.dto;

import lombok.*;
import newfarma.model.Estado;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class EstadoListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Estado> list;
}