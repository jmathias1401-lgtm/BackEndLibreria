package newfarma.audit;

import newfarma.model.Producto;
import newfarma.productos.ProductoRepositoryJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Auditoría JPA - CreatedDate, LastModifiedDate, CreatedBy")
class JpaAuditingTest {

    @Autowired
    private ProductoRepositoryJPA productoRepositoryJPA;

    @Test
    @DisplayName("Debe auditar automáticamente createdAt y createdBy como SYSTEM cuando no hay usuario")
    void shouldAuditCreationWithoutAuthenticatedUser() {
        Producto p = new Producto();
        p.setNombre("Producto Auditoria 1");
        p.setCodigoproducto("AUD_" + System.currentTimeMillis());

        Producto saved = productoRepositoryJPA.save(p);
        productoRepositoryJPA.flush();

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedBy()).isEqualTo("SYSTEM");
    }

    @Test
    @WithMockUser(username = "usuario_auditor")
    @DisplayName("Debe registrar el nombre del usuario autenticado en createdBy y lastModifiedBy")
    void shouldAuditWithAuthenticatedUser() {
        Producto p = new Producto();
        p.setNombre("Producto Auditoria 2");
        p.setCodigoproducto("AUD_USER_" + System.currentTimeMillis());

        Producto saved = productoRepositoryJPA.save(p);
        productoRepositoryJPA.flush();

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedBy()).isEqualTo("usuario_auditor");
        assertThat(saved.getLastModifiedBy()).isEqualTo("usuario_auditor");
    }
}

