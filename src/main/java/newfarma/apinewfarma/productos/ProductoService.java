package newfarma.apinewfarma.productos;

import newfarma.apinewfarma.model.Producto;
import newfarma.apinewfarma.productos.dto.ProductListResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductoService implements ProductoServiceI{
    ProductoRepositoryJPA productoRepositoryJPA;
    public ProductoService(ProductoRepositoryJPA productoRepositoryJPA){
        this.productoRepositoryJPA=productoRepositoryJPA;
    }

    @Override
    public ProductListResponse list(String params) {
        ProductListResponse response;
       /* int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);*/
        List<Producto> l = (List<Producto>) productoRepositoryJPA.findByCodigoproducto(params);

        Map result = new HashMap();
        response = ProductListResponse.builder()
                .page(1)
                .total(10)
                .list(l)
                .build();
        return response;
    }
}
