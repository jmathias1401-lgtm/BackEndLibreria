package newfarma.productos;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Override
    public BaseResponse save(Producto producto){
        BaseResponse response;
        Producto producto1;
        if(producto.getIdproducto()!=null)//actualiza un objeto existente
        {
            producto1=productoRepositoryJPA.findById(producto.getIdproducto()).get();
            producto1.setCodigoproducto(producto.getCodigoproducto());
            producto1.setNombre(producto.getNombre());
            producto1.setVencimiento(producto.getVencimiento());
            producto1.setEstado(producto.getEstado());
            producto1.setComposicion(producto.getComposicion());
            producto1.setUbicacion(producto.getUbicacion());
            producto1.setStock(producto.getStock());
            producto1.setPrecioventa(producto.getPrecioventa());
            producto1.setPrecioblister(producto.getPrecioblister());
            producto1.setPreciocaja(producto.getPreciocaja());
            producto1.setCodbarra(producto.getCodbarra());
            producto1.setPresentacion(producto.getPresentacion());
            producto1.setUnidadMedida(producto.getUnidadMedida());
            producto1.setLaboratorio(producto.getLaboratorio());
            productoRepositoryJPA.save(producto1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =productoRepositoryJPA.existsProductoByCodbarra(producto.getCodbarra());
            if (!existName){
                productoRepositoryJPA.save(producto);
                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }else{
                response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
            }
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Producto producto=productoRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (producto.getIdproducto()!=null)
        {
            productoRepositoryJPA.delete(producto);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
