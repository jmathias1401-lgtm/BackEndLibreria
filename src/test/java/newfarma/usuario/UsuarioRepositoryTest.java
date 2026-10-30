package newfarma.usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - UsuarioRepository")
class UsuarioRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Usuario> usuarioQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Usuario> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<String> exprUpper;

    @Mock
    private Expression<String> exprLower;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Usuario> usuarioTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private UsuarioRepository repository;

    private Usuario sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new Usuario();
        sampleUser.setIdusuario(1L);
        sampleUser.setNombreusuario("testuser");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            UserListRequest params = new UserListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Usuario.class)).thenReturn(usuarioQuery);
            when(usuarioQuery.from(Usuario.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(usuarioQuery.select(root)).thenReturn(usuarioQuery);
            when(usuarioQuery.where(any(Predicate[].class))).thenReturn(usuarioQuery);
            when(entityManager.createQuery(usuarioQuery)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.setMaxResults(10)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.setFirstResult(0)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.getResultList()).thenReturn(List.of(sampleUser));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleUser);
            verify(usuarioTypedQuery).setMaxResults(10);
            verify(usuarioTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtros de búsqueda 'search' aplica criteria")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            UserListRequest params = new UserListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("admin");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Usuario.class)).thenReturn(usuarioQuery);
            when(usuarioQuery.from(Usuario.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombreusuario")).thenReturn((Path) pathString);
            when(builder.upper(pathString)).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%ADMIN%"))).thenReturn(predicate);

            when(usuarioQuery.select(root)).thenReturn(usuarioQuery);
            when(usuarioQuery.where(any(Predicate[].class))).thenReturn(usuarioQuery);
            when(entityManager.createQuery(usuarioQuery)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.setMaxResults(10)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.setFirstResult(0)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.getResultList()).thenReturn(List.of(sampleUser));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%ADMIN%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombreusuario' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            UserListRequest params = new UserListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombreusuario("testuser");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Usuario.class)).thenReturn(usuarioQuery);
            when(usuarioQuery.from(Usuario.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombreusuario")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "testuser")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(usuarioQuery.select(root)).thenReturn(usuarioQuery);
            when(usuarioQuery.where(any(Predicate[].class))).thenReturn(usuarioQuery);
            when(entityManager.createQuery(usuarioQuery)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.setMaxResults(5)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.setFirstResult(0)).thenReturn(usuarioTypedQuery);
            when(usuarioTypedQuery.getResultList()).thenReturn(List.of(sampleUser));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "testuser");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            UserListRequest params = new UserListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Usuario.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(25L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(25L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
