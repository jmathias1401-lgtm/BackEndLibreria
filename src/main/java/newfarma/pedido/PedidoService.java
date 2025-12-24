package newfarma.pedido;

import jakarta.persistence.EntityNotFoundException;
import newfarma.pedido.dto.PedidoListRequest;
import newfarma.pedido.dto.PedidoListResponse;

import newfarma.model.Pedido;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PedidoService implements PedidoServiceI {
    private final PedidoRepository repository;
    private final PedidoRepositoryJPA repositoryJPA;
    public PedidoService(PedidoRepository repository, PedidoRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public PedidoListResponse list(PedidoListRequest params) {
        PedidoListResponse response;

        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Pedido> l = (List<Pedido>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = PedidoListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Pedido pedido) {
        BaseResponse response;
        Pedido pedido1;
        if(pedido.getIdpedido()!=null)//actualiza un objeto existente
        {
            pedido1=repositoryJPA.findById(pedido.getIdpedido()).get();
            pedido1.setFechapedido(pedido.getFechapedido());
            pedido1.setEstado(pedido.getEstado());
            pedido1.setCliente(pedido.getCliente());
            pedido1.setProducto(pedido.getProducto());
            pedido1.setCodigopedido(pedido.getCodigopedido());
            repositoryJPA.save(pedido1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            repositoryJPA.save(pedido);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Pedido pedido=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (pedido.getIdpedido()!=null)
        {
            repositoryJPA.delete(pedido);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
    @Override
    public String lastPedido()
    {
        return this.repositoryJPA.lastPedido().toString();
    }

    @Override
    public Pedido findById(Long id) {
        return null;
    }
}