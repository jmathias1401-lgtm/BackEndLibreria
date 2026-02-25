package newfarma.detallecompra;

import newfarma.model.detallecompra;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.detallecompra.dto.DetalleCompraListResponse;
import newfarma.utils.BaseResponse;

public interface DetalleCompraServiceI {
    DetalleCompraListResponse list(DetalleCompraListRequest params);

    public DetalleCompraListResponse DetalleCompraListById(int id);

    DetalleCompraListResponse listDetalleCompraByProductoId(int id);

    BaseResponse save(detallecompra detalleCompra);

    BaseResponse eliminar(Long id);

    int CountDetalleCompra();
}