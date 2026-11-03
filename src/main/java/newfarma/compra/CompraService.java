package newfarma.compra;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Compra;
import newfarma.compra.dto.CompraListRequest;
import newfarma.compra.dto.CompraListResponse;
import newfarma.compra.dto.CompraRequestDto;
import newfarma.utils.BaseResponse;
import newfarma.utils.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService implements CompraServiceI {
    private final compraRepository repository;
    CompraRepositoryJPA compraRepositoryJPA;

    public CompraService(CompraRepositoryJPA compraRepositoryJPA, compraRepository repository) {
        this.compraRepositoryJPA = compraRepositoryJPA;
        this.repository = repository;
    }

    @Override
    public CompraListResponse list(CompraListRequest params) {
        Pageable pageable = PageableUtils.createPageable(params.getPage(), params.getXpage());
        params.setPage(pageable.getPageNumber() + 1);
        params.setXpage(pageable.getPageSize());
        params.setOffset((int) pageable.getOffset());

        List<Compra> l = (List<Compra>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        return CompraListResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .total(total)
                .xpage(pageable.getPageSize())
                .list(l)
                .build();
    }

    @Override
    public BaseResponse save(CompraRequestDto compraDto) {
        Compra compra = new Compra();
        compra.setIdcompra(compraDto.getIdCompra());
        compra.setSerie(compraDto.getSerie());
        compra.setCorrelativo(compraDto.getCorrelativo());
        compra.setFechacompra(compraDto.getFechaCompra());
        compra.setCostocompra(compraDto.getCostoCompra());
        compra.setProveedor(compraDto.getProveedor());
        compra.setEmpleado(compraDto.getEmpleado());
        compra.setTipocomprobante(compraDto.getTipoComprobante());
        return save(compra);
    }

    @Override
    public BaseResponse save(Compra compra) {
        BaseResponse response;
        Compra compra1;
        if (compra.getIdcompra() != null) {
            compra1 = compraRepositoryJPA.findById(compra.getIdcompra()).get();
            compra1.setCorrelativo(compra.getCorrelativo());
            compra1.setSerie(compra.getSerie());
            compra1.setFechacompra(compra.getFechacompra());
            compra1.setCostocompra(compra.getCostocompra());
            compra1.setProveedor(compra.getProveedor());
            compra1.setEmpleado(compra.getEmpleado());
            compra1.setTipocomprobante(compra.getTipocomprobante());
            compraRepositoryJPA.save(compra1);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        } else {
            boolean existName = compraRepositoryJPA.existsCompraByCorrelativo(compra.getCorrelativo());
            if (existName) {
                response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
            } else {
                compraRepositoryJPA.save(compra);
                response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }
        }
        return response;
    }

    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Compra compra = compraRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (compra.getIdcompra() != null) {
            compraRepositoryJPA.delete(compra);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        } else {
            response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
