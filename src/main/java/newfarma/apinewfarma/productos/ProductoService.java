package newfarma.apinewfarma.productos;

import newfarma.apinewfarma.model.Producto;
import newfarma.apinewfarma.productos.dto.ProductListRequest;
import newfarma.apinewfarma.productos.dto.ProductListResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductoService implements ProductoServiceI{

    private final ProductoRepository repository;
    ProductoRepositoryJPA productoRepositoryJPA;
    public ProductoService(ProductoRepositoryJPA productoRepositoryJPA,ProductoRepository repository){
        this.productoRepositoryJPA=productoRepositoryJPA;
        this.repository=repository;
    }

    @Override
    public ProductListResponse list(ProductListRequest params) {
        ProductListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Producto> l = (List<Producto>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = ProductListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
}
