package newfarma.unidadmedida.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class UnidadMedidaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String codbarra;
    String search;
}
