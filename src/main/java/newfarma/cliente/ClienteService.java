package newfarma.cliente;

import jakarta.persistence.EntityNotFoundException;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteRequestDto;
import newfarma.cliente.dto.ClienteResponse;
import newfarma.model.Cliente;
import newfarma.model.Persona;
import newfarma.persona.PersonaService;
import newfarma.utils.BaseResponse;
import newfarma.utils.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService implements ClienteServiceI {
    private final ClienteRepository repository;
    private final ClienteRepositoryJPA repositoryJPA;
    private final PersonaService ServicioPersona;

    public ClienteService(ClienteRepository repository, ClienteRepositoryJPA repositoryJPA, PersonaService ServicioPersona) {
        this.repository = repository;
        this.repositoryJPA = repositoryJPA;
        this.ServicioPersona = ServicioPersona;
    }

    @Override
    public ClienteResponse list(ClienteListRequest params) {
        Pageable pageable = PageableUtils.createPageable(params.getPage(), params.getXpage());
        params.setPage(pageable.getPageNumber() + 1);
        params.setXpage(pageable.getPageSize());
        params.setOffset((int) pageable.getOffset());

        List<Cliente> l = (List<Cliente>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        return ClienteResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .total(total)
                .xpage(pageable.getPageSize())
                .list(l)
                .build();
    }

    @Override
    public BaseResponse save(ClienteRequestDto clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setIdcliente(clienteDto.getIdCliente());
        cliente.setPersona(clienteDto.getPersona());
        return save(cliente);
    }

    @Override
    public BaseResponse save(Cliente cliente) {
        BaseResponse response;
        Cliente cliente1;
        if (cliente.getIdcliente() != null) {
            cliente1 = repositoryJPA.findById(cliente.getIdcliente()).get();
            cliente1.setPersona(cliente.getPersona());
            repositoryJPA.save(cliente1);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        } else {
            Persona persona = ServicioPersona.buscarPorDni(cliente.getPersona().getDni());
            if (persona == null) {
                return BaseResponse.builder().status(404).code(String.valueOf(HttpStatus.NOT_FOUND)).message("PERSONA NO ENCONTRADA").build();
            }
            boolean existPersonaEnCliente = repositoryJPA.existsClienteByPersonaId(persona.getIdpersona());
            if (existPersonaEnCliente) {
                response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
            } else {
                cliente.setPersona(persona);
                repositoryJPA.save(cliente);
                response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }
        }
        return response;
    }

    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Cliente cliente = repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (cliente.getIdcliente() != null) {
            repositoryJPA.delete(cliente);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        } else {
            response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
