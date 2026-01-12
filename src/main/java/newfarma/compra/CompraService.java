package newfarma.compra;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Compra;
import newfarma.compra.dto.CompraListRequest;
import newfarma.compra.dto.CompraListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService implements CompraServiceI {
    private final compraRepository repository;
    CompraRepositoryJPA compraRepositoryJPA;
    public CompraService(CompraRepositoryJPA compraRepositoryJPA, compraRepository repository){
        this.compraRepositoryJPA=compraRepositoryJPA;
        this.repository=repository;
    }
    @Override
    public CompraListResponse list(CompraListRequest params) {
        CompraListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Compra> l = (List<Compra>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = CompraListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Compra compra){
        BaseResponse response;
        Compra compra1;
        if(compra.getIdcompra()!=null)//actualiza un objeto existente
        {
            compra1=compraRepositoryJPA.findById(compra.getIdcompra()).get();
            compra1.setCorrelativo(compra.getCorrelativo());
            compra1.setSerie(compra.getSerie());
            compra1.setFechacompra(compra.getFechacompra());
            compra1.setCostocompra(compra.getCostocompra());
            compra1.setProveedor(compra.getProveedor());
            compra1.setEmpleado(compra.getEmpleado());
            compra1.setTipoComprobante(compra.getTipoComprobante());
            compraRepositoryJPA.save(compra1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existCorrelativo =compraRepositoryJPA.existsCompraByCorrelativo(compra.getCorrelativo());
            boolean existeSerie=compraRepositoryJPA.existsCompraBySerieAndAndCorrelativo(compra.getCorrelativo(),compra.getSerie());
            if (!existeSerie){
               Long idcompra= compraRepositoryJPA.save(compra).getIdcompra();

                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY")
                        .idCompra(idcompra)
                        .build();
            }else{
                response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS BY CORRELATIVO EXISTE IN DB").build();
            }
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Compra compra=compraRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (compra.getIdcompra()!=null)
        {
            compraRepositoryJPA.delete(compra);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}