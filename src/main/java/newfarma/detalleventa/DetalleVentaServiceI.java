package newfarma.detalleventa;

import newfarma.model.DetalleVenta;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.utils.BaseResponse;

public interface DetalleVentaServiceI {
    DetalleVentaListResponse list(DetalleVentaListRequest params);

    public DetalleVentaListResponse DetalleVentaListById(int id);

    BaseResponse save(DetalleVenta detalleVenta);

    BaseResponse eliminar(Long id);

    int CountDetalleVenta();
}