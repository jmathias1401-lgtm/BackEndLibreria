package newfarma.empleado.dto;

import lombok.*;
import newfarma.model.Empleado;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class EmpleadoListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Empleado> list;
}