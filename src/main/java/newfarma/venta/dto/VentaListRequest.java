package newfarma.venta.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class VentaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String correlativo;
    String serie;
    String search;

}
