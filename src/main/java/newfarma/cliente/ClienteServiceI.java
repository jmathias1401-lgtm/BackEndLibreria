package newfarma.cliente;

import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteResponse;
import newfarma.model.Cliente;
import newfarma.utils.BaseResponse;

public interface ClienteServiceI {
    ClienteResponse list(ClienteListRequest params);
    BaseResponse save(Cliente cliente);
    BaseResponse eliminar(Long id);
}
