package newfarma.usuario.dto;

import lombok.*;
import newfarma.model.Usuario;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class UserListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Usuario> list;
}