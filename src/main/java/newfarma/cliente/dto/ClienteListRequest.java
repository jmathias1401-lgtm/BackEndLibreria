package newfarma.cliente.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class ClienteListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombrecliente;
    String search;
    Integer idcliente;
    Integer persona_idpersona;
}
