package newfarma.usuario;

import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
import newfarma.utils.BaseResponse;

public interface UsuarioServiceI {
    UserListResponse list(UserListRequest params);

    public UserListResponse UserListById(Long id);

    BaseResponse save(Usuario usuario);

    BaseResponse eliminar(Long id);

    int CountUsuario();
}