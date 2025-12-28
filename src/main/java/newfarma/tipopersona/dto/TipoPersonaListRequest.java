package newfarma.tipopersona.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class TipoPersonaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String search;
    int id;
}
