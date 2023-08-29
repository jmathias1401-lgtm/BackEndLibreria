package newfarma.venta;

import newfarma.model.Persona;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.utils.BaseResponse;

public interface VentaServiceI {
    VentaListResponse list(VentaListRequest params);

    BaseResponse save(Persona persona);

    BaseResponse eliminar(Long id);
}
