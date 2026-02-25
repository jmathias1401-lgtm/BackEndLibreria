package newfarma.usuario;

import newfarma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryJPA extends JpaRepository<Usuario,Long> {
    @Query(value = "from Usuario u where u.nombreusuario = ?1 ")
    public Optional<Usuario> findByNombreusuario(String nombreusuario);
    public boolean existsUsuarioByNombreusuario(String nombreusuario);

    @Query( value = "select * from Usuario u where u.idusuario =?1",  nativeQuery = true)
    public List<Usuario> listUsuariosById(Long userId);

    @Query(value = "select count(*) from Usuario",nativeQuery = true)
    public Integer CountUsuario();

}