package newfarma.detalleventa;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.detalleventa;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaService implements DetalleVentaServiceI{

    private final DetalleVentaRepository repository;
    DetalleVentaRepositoryJPA detalleVentaRepositoryJPA;
    public DetalleVentaService(DetalleVentaRepositoryJPA detalleVentaRepositoryJPA,DetalleVentaRepository repository){
        this.detalleVentaRepositoryJPA=detalleVentaRepositoryJPA;
        this.repository=repository;
    }

    @Override
    public DetalleVentaListResponse list(DetalleVentaListRequest params) {
        DetalleVentaListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<detalleventa> l = (List<detalleventa>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = DetalleVentaListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public DetalleVentaListResponse DetalleVentaListById(int id){
        DetalleVentaListResponse response;
        List<detalleventa> listDetalleVenta = detalleVentaRepositoryJPA.listDetalleVentaById(id);

        response =DetalleVentaListResponse.builder()
                .page(listDetalleVenta.size())
                .total(listDetalleVenta.size())
                .xpage(listDetalleVenta.size())
                .list(listDetalleVenta)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(detalleventa detalleVenta){
        BaseResponse response;
        detalleventa detalleVenta1;
        if(detalleVenta.getIddetalleventa()!=null && detalleVenta.getIddetalleventa()!=0)//actualiza un objeto existente
        {
            detalleVenta1=detalleVentaRepositoryJPA.findById(detalleVenta.getIddetalleventa()).get();
            detalleVenta1.setCodigodetalleventa(detalleVenta.getCodigodetalleventa());
            detalleVenta1.setUnidades(detalleVenta.getUnidades());
            detalleVenta1.setCostounidad(detalleVenta.getCostounidad());
            detalleVenta1.setSubtotal(detalleVenta.getSubtotal());
            detalleVenta1.setDescuentounidad(detalleVenta.getDescuentounidad());
            detalleVenta1.setTotal(detalleVenta.getTotal());
            detalleVenta1.setVenta(detalleVenta.getVenta());
            detalleVenta1.setProducto(detalleVenta.getProducto());
            detalleVentaRepositoryJPA.save(detalleVenta1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existCode =detalleVentaRepositoryJPA.existsDetalleVentaByCodigodetalleventa(detalleVenta.getCodigodetalleventa());
            if (!existCode){
                detalleVentaRepositoryJPA.save(detalleVenta);
                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }else{
                response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SAVED BY CODIGODETALLEVENTA EXIST IN DB ").build();
            }
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        detalleventa detalleVenta=detalleVentaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (detalleVenta.getIddetalleventa()!=null)
        {
            detalleVentaRepositoryJPA.delete(detalleVenta);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
    @Override
    public int CountDetalleVenta()
    {
        return detalleVentaRepositoryJPA.CountDetalleVenta();
    }

    @Override
    public DetalleVentaListResponse ventasMes(int mes, int anio) {
        List<detalleventa> listDetalleVenta = detalleVentaRepositoryJPA.ventasMes(mes,anio);
        DetalleVentaListResponse response = DetalleVentaListResponse.builder()
                .page(listDetalleVenta.size())
                .total(listDetalleVenta.size())
                .xpage(listDetalleVenta.size())
                .list(listDetalleVenta)
                .build();
        return response;
    }
}