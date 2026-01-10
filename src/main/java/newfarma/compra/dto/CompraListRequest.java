package newfarma.compra.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class CompraListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String correlativo;
    String serie;
    String search;

}