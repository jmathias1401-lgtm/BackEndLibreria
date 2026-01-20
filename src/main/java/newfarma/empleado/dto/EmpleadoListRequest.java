package newfarma.empleado.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class EmpleadoListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    Long persona_idpersona;
    Long estado_idestado;
    Long usuario_idusuario;
    Long idempleado;
    String search;
}