package newfarma.detalleventa;

import newfarma.model.detalleventa;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.detalleventa.dto.DetalleVentaRequestDto;
import newfarma.detalleventa.dto.VentaTrazabilidadDto;
import newfarma.utils.BaseResponse;

import java.util.List;

public interface DetalleVentaServiceI {
    DetalleVentaListResponse list(DetalleVentaListRequest params);

    DetalleVentaListResponse DetalleVentaListById(int id);

    BaseResponse save(DetalleVentaRequestDto dto);

    BaseResponse save(detalleventa detalleVenta);

    BaseResponse eliminar(Long id);

    int CountDetalleVenta();

    DetalleVentaListResponse ventasMes(int mes, int anio);

    List<VentaTrazabilidadDto> getTrazabilidadVentas(Long idProducto);
}
