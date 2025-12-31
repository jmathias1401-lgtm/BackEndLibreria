package newfarma.proveedor;

import newfarma.model.Proveedor;
import newfarma.proveedor.dto.ProveedorListRequest;
import newfarma.proveedor.dto.ProveedorResponse;
import newfarma.utils.BaseResponse;

public interface ProveedorServiceI {
    ProveedorResponse list(ProveedorListRequest params);
    BaseResponse save(Proveedor proveedor);
    BaseResponse eliminar(Long id);
}