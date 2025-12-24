package newfarma.pedido;

import newfarma.pedido.dto.PedidoListRequest;
import newfarma.pedido.dto.PedidoListResponse;
import newfarma.model.Pedido;
import newfarma.utils.BaseResponse;

public interface PedidoServiceI {
    PedidoListResponse list(PedidoListRequest params);
    BaseResponse save(Pedido pedido);
    BaseResponse eliminar(Long id);

    String lastPedido();
    Pedido findById(Long id);
}