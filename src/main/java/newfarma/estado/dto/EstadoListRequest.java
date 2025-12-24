package newfarma.estado.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class EstadoListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String estado;
    String search;
}