package newfarma.persona.dto;

import lombok.*;
import newfarma.model.Producto;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class PersonaListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Producto> list;
}
