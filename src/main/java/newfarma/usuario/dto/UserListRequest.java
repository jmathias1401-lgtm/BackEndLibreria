package newfarma.usuario.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class UserListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombreusuario;
    String search;
    Long id;
}