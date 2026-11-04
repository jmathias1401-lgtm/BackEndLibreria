package newfarma.detalleventa;

import newfarma.model.detalleventa;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.detalleventa.dto.DetalleVentaRequestDto;
import newfarma.utils.BaseResponse;

public interface DetalleVentaServiceI {
    DetalleVentaListResponse list(DetalleVentaListRequest params);

    DetalleVentaListResponse DetalleVentaListById(int id);

    BaseResponse save(DetalleVentaRequestDto dto);

    BaseResponse save(detalleventa detalleVenta);

    BaseResponse eliminar(Long id);

    int CountDetalleVenta();

    DetalleVentaListResponse ventasMes(int mes, int anio);
}
