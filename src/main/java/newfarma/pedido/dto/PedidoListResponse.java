package newfarma.pedido.dto;

import lombok.*;
import newfarma.model.Pedido;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class PedidoListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Pedido> list;
}