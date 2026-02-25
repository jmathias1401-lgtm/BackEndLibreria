package newfarma.unidadmedida.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class UnidadMedidaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombreunidad;

    Integer idunidadmedida;
}
