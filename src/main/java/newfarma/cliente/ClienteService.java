package newfarma.cliente;

import jakarta.persistence.EntityNotFoundException;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteResponse;

import newfarma.model.Cliente;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClienteService implements ClienteServiceI {
    private final ClienteRepository repository;
    private final ClienteRepositoryJPA repositoryJPA;
    public ClienteService(ClienteRepository repository, ClienteRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public ClienteResponse list(ClienteListRequest params) {
        ClienteResponse response;

        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Cliente> l = (List<Cliente>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = ClienteResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Cliente cliente) {
        BaseResponse response;
        Cliente cliente1;
        if(cliente.getIdcliente()!=null)//actualiza un objeto existente
        {
            cliente1=repositoryJPA.findById(cliente.getIdcliente()).get();
            cliente1.setPersona(cliente.getPersona());

            repositoryJPA.save(cliente1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            repositoryJPA.save(cliente);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Cliente cliente=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (cliente.getIdcliente()!=null)
        {
            repositoryJPA.delete(cliente);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
