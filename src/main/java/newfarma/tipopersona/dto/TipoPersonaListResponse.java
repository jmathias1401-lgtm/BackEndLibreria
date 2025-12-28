package newfarma.tipopersona.dto;

import lombok.*;
import newfarma.model.TipoPersona;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class TipoPersonaListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<TipoPersona> list;
}
