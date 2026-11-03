package newfarma.venta;

import newfarma.model.Venta;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.venta.dto.VentaRequestDto;
import newfarma.utils.BaseResponse;

public interface VentaServiceI {
    VentaListResponse list(VentaListRequest params);

    BaseResponse save(VentaRequestDto ventaDto);

    BaseResponse save(Venta venta);

    BaseResponse eliminar(Long id);
}
