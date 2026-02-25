package newfarma.usuario;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UsuarioService implements UsuarioServiceI{

    private final UsuarioRepository repository;
    UsuarioRepositoryJPA usuarioRepositoryJPA;

    public UsuarioService(UsuarioRepositoryJPA usuarioRepositoryJPA,UsuarioRepository repository){
        this.usuarioRepositoryJPA=usuarioRepositoryJPA;
        this.repository=repository;
    }

    @Override
    public UserListResponse list(UserListRequest params) {
        UserListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Usuario> l = (List<Usuario>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = UserListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public UserListResponse UserListById(Long id){
        UserListResponse response;
        List<Usuario> listUsuarios = usuarioRepositoryJPA.listUsuariosById(id);

        response =UserListResponse.builder()
                .page(listUsuarios.size())
                .total(listUsuarios.size())
                .xpage(listUsuarios.size())
                .list(listUsuarios)
                .build();
        return response;
    }
    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getIdusuario() != null && usuario.getIdusuario() != 0) {
            Usuario usuario1 = usuarioRepositoryJPA.findById(usuario.getIdusuario()).get();
            usuario1.setNombreusuario(usuario.getNombreusuario());
            usuario1.setClave(usuario.getClave());
            usuario1.setFechacreacion(usuario.getFechacreacion());
            usuario1.setCargo(usuario.getCargo());
            return usuarioRepositoryJPA.save(usuario1);
        } else {
            boolean existName = usuarioRepositoryJPA.existsUsuarioByNombreusuario(usuario.getNombreusuario());
            if (!existName) {
                usuario.setFechacreacion(new Date());
                return usuarioRepositoryJPA.save(usuario);
            } else {
                return null;
            }
        }
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Usuario usuario=usuarioRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (usuario.getIdusuario()!=null)
        {
            usuarioRepositoryJPA.delete(usuario);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
    @Override
    public int CountUsuario()
    {
        return usuarioRepositoryJPA.CountUsuario();
    }
}