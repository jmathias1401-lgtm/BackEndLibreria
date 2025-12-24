package newfarma.cliente.dto;

import lombok.*;
import newfarma.model.Cliente;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ClienteResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Cliente> list;
}
