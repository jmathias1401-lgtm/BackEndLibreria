package newfarma.productos;

import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.utils.BaseResponse;

public interface ProductoServiceI {
    ProductListResponse list(ProductListRequest params);

    BaseResponse save(Producto producto);

    BaseResponse eliminar(Long id);
}
