package newfarma.detalleventa;

import newfarma.model.detalleventa;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.utils.BaseResponse;

public interface DetalleVentaServiceI {
    DetalleVentaListResponse list(DetalleVentaListRequest params);

    public DetalleVentaListResponse DetalleVentaListById(int id);

    BaseResponse save(detalleventa detalleVenta);

    BaseResponse eliminar(Long id);

    int CountDetalleVenta();
}