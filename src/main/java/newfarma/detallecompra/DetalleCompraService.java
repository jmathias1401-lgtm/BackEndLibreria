package newfarma.detallecompra;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.DetalleCompra;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.detallecompra.dto.DetalleCompraListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleCompraService implements DetalleCompraServiceI{

    private final DetalleCompraRepository repository;
    DetalleCompraRepositoryJPA detalleCompraRepositoryJPA;
    public DetalleCompraService(DetalleCompraRepositoryJPA detalleCompraRepositoryJPA,DetalleCompraRepository repository){
        this.detalleCompraRepositoryJPA=detalleCompraRepositoryJPA;
        this.repository=repository;
    }

    @Override
    public DetalleCompraListResponse list(DetalleCompraListRequest params) {
        DetalleCompraListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<DetalleCompra> l = (List<DetalleCompra>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = DetalleCompraListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public DetalleCompraListResponse DetalleCompraListById(int id){
        DetalleCompraListResponse response;
        List<DetalleCompra> listDetalleCompra = detalleCompraRepositoryJPA.listDetalleCompraById(id);

        response =DetalleCompraListResponse.builder()
                .page(listDetalleCompra.size())
                .total(listDetalleCompra.size())
                .xpage(listDetalleCompra.size())
                .list(listDetalleCompra)
                .build();
        return response;
    }

    @Override
    public DetalleCompraListResponse listDetalleCompraByProductoId(int id){
        DetalleCompraListResponse response;
        Double listdetallecomprabyproductid=detalleCompraRepositoryJPA.listDetalleCompraByProductoId(id);

        response =DetalleCompraListResponse.builder()
                .page(0)
                .total(0)
                .xpage(0)
                .lastCost(listdetallecomprabyproductid)
                .build();
        return response;
    }

    @Override
    public BaseResponse save(DetalleCompra detalleCompra){
        BaseResponse response;
        DetalleCompra detalleCompra1;
        if(detalleCompra.getIddetallecompra()!=null && detalleCompra.getIddetallecompra()!=0)//actualiza un objeto existente
        {
            detalleCompra1=detalleCompraRepositoryJPA.findById(detalleCompra.getIddetallecompra()).get();
            detalleCompra1.setCodigodetallecompra(detalleCompra.getCodigodetallecompra());
            detalleCompra1.setUnidades(detalleCompra.getUnidades());
            detalleCompra1.setCostounidad(detalleCompra.getCostounidad());
            detalleCompra1.setTotal(detalleCompra.getTotal());
            detalleCompra1.setCompra(detalleCompra.getCompra());
            detalleCompra1.setProducto(detalleCompra.getProducto());
            detalleCompraRepositoryJPA.save(detalleCompra1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existCode =detalleCompraRepositoryJPA.existsDetalleCompraByCodigodetallecompra(detalleCompra.getCodigodetallecompra());
            if (!existCode){
                detalleCompraRepositoryJPA.save(detalleCompra);
                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }else{
                response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SAVED BY CODIGODETALLECOMPRA EXIST IN DB ").build();
            }
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        DetalleCompra detalleCompra=detalleCompraRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (detalleCompra.getIddetallecompra()!=null)
        {
            detalleCompraRepositoryJPA.delete(detalleCompra);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
    @Override
    public int CountDetalleCompra()
    {
        return detalleCompraRepositoryJPA.CountDetalleCompra();
    }
}