package newfarma.usuario;

import newfarma.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - UsuarioRepositoryJPA")
class UsuarioRepositoryJPATest {

    @Autowired
    private UsuarioRepositoryJPA usuarioRepositoryJPA;

    @Test
    @DisplayName("findByNombreusuario retorna usuario si existe")
    void findByNombreusuario_ShouldReturnUserWhenExists() {
        Usuario u = new Usuario();
        u.setNombreusuario("testuserjpa_" + System.currentTimeMillis());
        u.setClave("pass123");
        usuarioRepositoryJPA.save(u);

        Optional<Usuario> result = usuarioRepositoryJPA.findByNombreusuario(u.getNombreusuario());

        assertThat(result).isPresent();
        assertThat(result.get().getNombreusuario()).isEqualTo(u.getNombreusuario());
    }

    @Test
    @DisplayName("existsUsuarioByNombreusuario retorna true si existe")
    void existsUsuarioByNombreusuario_ShouldReturnTrue() {
        Usuario u = new Usuario();
        u.setNombreusuario("testexist_" + System.currentTimeMillis());
        u.setClave("pass123");
        usuarioRepositoryJPA.save(u);

        boolean exists = usuarioRepositoryJPA.existsUsuarioByNombreusuario(u.getNombreusuario());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("listUsuariosById retorna lista con el usuario")
    void listUsuariosById_ShouldReturnList() {
        Usuario u = new Usuario();
        u.setNombreusuario("testidlist_" + System.currentTimeMillis());
        u.setClave("pass123");
        Usuario saved = usuarioRepositoryJPA.save(u);

        List<Usuario> list = usuarioRepositoryJPA.listUsuariosById(saved.getIdusuario());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getIdusuario()).isEqualTo(saved.getIdusuario());
    }

    @Test
    @DisplayName("CountUsuario retorna conteo de usuarios")
    void countUsuario_ShouldReturnCount() {
        Integer count = usuarioRepositoryJPA.CountUsuario();

        assertThat(count).isNotNull();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }
}
