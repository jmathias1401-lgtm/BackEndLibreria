package newfarma.venta;

import newfarma.model.Persona;
import newfarma.model.Venta;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.utils.BaseResponse;

public interface VentaServiceI {
    VentaListResponse list(VentaListRequest params);

    BaseResponse save(Venta persona);

    BaseResponse eliminar(Long id);
}
