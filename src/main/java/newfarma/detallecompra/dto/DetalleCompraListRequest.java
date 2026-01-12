package newfarma.detallecompra.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class DetalleCompraListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String codigodetallecompra;
    String search;
    Long idcompra;
}