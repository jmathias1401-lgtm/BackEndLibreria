package newfarma.venta;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Persona;
import newfarma.model.Producto;
import newfarma.model.Venta;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService implements VentaServiceI {
    private final ventaRepository repository;
    VentaRepositoryJPA ventaRepositoryJPA;
    public VentaService(VentaRepositoryJPA ventaRepositoryJPA, ventaRepository repository){
        this.ventaRepositoryJPA=ventaRepositoryJPA;
        this.repository=repository;
    }
    @Override
    public VentaListResponse list(VentaListRequest params) {
        VentaListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Venta> l = (List<Venta>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = VentaListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Venta venta){
        BaseResponse response;
        Venta venta1;
        if(venta.getIdventa()!=null)//actualiza un objeto existente
        {
            venta1=ventaRepositoryJPA.findById(venta.getIdventa()).get();
            venta1.setCorrelativo(venta.getCorrelativo());
            venta1.setSerie(venta.getSerie());
            venta1.setFechaventa(venta.getFechaventa());
            venta1.setIgv(venta.getIgv());
            venta1.setSubtotal(venta.getSubtotal());
            venta1.setCostoventa(venta.getCostoventa());
            venta1.setCliente(venta.getCliente());
            venta1.setEmpleado(venta.getEmpleado());
            venta1.setTipoComprobante(venta.getTipoComprobante());
            ventaRepositoryJPA.save(venta1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =ventaRepositoryJPA.existsVentaByCorrelativo(venta.getCorrelativo());
            if (!existName){
               Long idventa= ventaRepositoryJPA.save(venta).getIdventa();

                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY")
                        .idVenta(idventa)
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
        Venta venta=ventaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (venta.getIdventa()!=null)
        {
            ventaRepositoryJPA.delete(venta);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
