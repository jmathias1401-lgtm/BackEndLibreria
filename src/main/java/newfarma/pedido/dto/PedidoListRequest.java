package newfarma.pedido.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PedidoListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String search;
}