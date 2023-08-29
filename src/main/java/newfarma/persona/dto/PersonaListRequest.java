package newfarma.persona.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PersonaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String codbarra;
    String search;

}
