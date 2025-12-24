package newfarma.detalleventa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class DetalleVentaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String codigodetalleventa;
    String search;
    Long idventa;
}