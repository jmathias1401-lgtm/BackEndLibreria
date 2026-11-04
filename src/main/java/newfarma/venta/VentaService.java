package newfarma.venta;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Persona;
import newfarma.model.Producto;
import newfarma.model.Venta;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.venta.dto.VentaRequestDto;
import newfarma.utils.BaseResponse;
import newfarma.utils.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VentaService implements VentaServiceI {
    private final ventaRepository repository;
    VentaRepositoryJPA ventaRepositoryJPA;

    public VentaService(VentaRepositoryJPA ventaRepositoryJPA, ventaRepository repository){
        this.ventaRepositoryJPA = ventaRepositoryJPA;
        this.repository = repository;
    }

    @Override
    public VentaListResponse list(VentaListRequest params) {
        Pageable pageable = PageableUtils.createPageable(params.getPage(), params.getXpage());
        params.setPage(pageable.getPageNumber() + 1);
        params.setXpage(pageable.getPageSize());
        params.setOffset((int) pageable.getOffset());

        List<Venta> l = (List<Venta>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        return VentaListResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .total(total)
                .xpage(pageable.getPageSize())
                .list(l)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(VentaRequestDto ventaDto) {
        Venta venta = new Venta();
        venta.setIdventa(ventaDto.getIdVenta());
        venta.setSerie(ventaDto.getSerie());
        venta.setCorrelativo(ventaDto.getCorrelativo());
        venta.setFechaventa(ventaDto.getFechaVenta());
        venta.setIgv(ventaDto.getIgv());
        venta.setSubtotal(ventaDto.getSubtotal());
        venta.setCostoventa(ventaDto.getCostoVenta());
        venta.setCliente(ventaDto.getCliente());
        venta.setEmpleado(ventaDto.getEmpleado());
        venta.setTipoComprobante(ventaDto.getTipoComprobante());
        return save(venta);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(Venta venta){
        BaseResponse response;
        Venta venta1;
        if(venta.getIdventa() != null) // actualiza un objeto existente
        {
            venta1 = ventaRepositoryJPA.findById(venta.getIdventa()).get();
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
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        } else // crea un nuevo objeto
        {
            boolean existName = ventaRepositoryJPA.existsVentaByCorrelativo(venta.getCorrelativo());
            if (!existName){
               Long idventa = ventaRepositoryJPA.save(venta).getIdventa();

                response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY")
                        .idVenta(idventa)
                        .build();
            } else {
                response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS BY CORRELATIVO EXISTE IN DB").build();
            }
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Venta venta = ventaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (venta.getIdventa() != null)
        {
            ventaRepositoryJPA.delete(venta);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        } else {
            response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
