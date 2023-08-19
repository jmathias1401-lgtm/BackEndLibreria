package newfarma.apinewfarma.productos;

import newfarma.apinewfarma.productos.dto.ProductListRequest;
import newfarma.apinewfarma.productos.dto.ProductListResponse;

public interface ProductoServiceI {
    ProductListResponse list(ProductListRequest params);
}
