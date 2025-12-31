package newfarma.proveedor.dto;

import lombok.*;
import newfarma.model.Proveedor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ProveedorResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Proveedor> list;
}