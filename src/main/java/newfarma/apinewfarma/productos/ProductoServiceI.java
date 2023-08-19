package newfarma.apinewfarma.productos;

import newfarma.apinewfarma.productos.dto.ProductListResponse;

public interface ProductoServiceI {
    ProductListResponse list(String params);
}
