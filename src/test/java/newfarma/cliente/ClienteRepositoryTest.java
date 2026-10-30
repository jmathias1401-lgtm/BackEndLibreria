package newfarma.cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.model.Cliente;
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
@DisplayName("Pruebas Unitarias - ClienteRepository")
class ClienteRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Cliente> clienteQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Cliente> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<Object> pathNested;

    @Mock
    private Expression<String> exprString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Order order;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Cliente> clienteTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private ClienteRepository repository;

    private Cliente sampleCliente;

    @BeforeEach
    void setUp() {
        sampleCliente = new Cliente();
        sampleCliente.setIdcliente(1L);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            ClienteListRequest params = new ClienteListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Cliente.class)).thenReturn(clienteQuery);
            when(clienteQuery.from(Cliente.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(clienteQuery.orderBy(order)).thenReturn(clienteQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(clienteQuery.select(root)).thenReturn(clienteQuery);
            when(clienteQuery.where(any(Predicate[].class))).thenReturn(clienteQuery);
            when(entityManager.createQuery(clienteQuery)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.setMaxResults(10)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.setFirstResult(0)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.getResultList()).thenReturn(List.of(sampleCliente));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleCliente);
            verify(clienteTypedQuery).setMaxResults(10);
            verify(clienteTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de persona_idpersona aplica filtro relacional")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithPersonaFilter_ShouldApplyRelationalFilter() {
            ClienteListRequest params = new ClienteListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setPersona_idpersona(5);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Cliente.class)).thenReturn(clienteQuery);
            when(clienteQuery.from(Cliente.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(clienteQuery.orderBy(order)).thenReturn(clienteQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("persona")).thenReturn((Path) pathObject);
            when(pathObject.get("idpersona")).thenReturn((Path) pathNested);
            when(builder.equal(pathNested, 5)).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(clienteQuery.select(root)).thenReturn(clienteQuery);
            when(clienteQuery.where(any(Predicate[].class))).thenReturn(clienteQuery);
            when(entityManager.createQuery(clienteQuery)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.setMaxResults(10)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.setFirstResult(0)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.getResultList()).thenReturn(List.of(sampleCliente));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathNested, 5);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            ClienteListRequest params = new ClienteListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("100");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Cliente.class)).thenReturn(clienteQuery);
            when(clienteQuery.from(Cliente.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(clienteQuery.orderBy(order)).thenReturn(clienteQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("idcliente")).thenReturn((Path) pathObject);
            when(pathObject.as(String.class)).thenReturn(exprString);
            when(builder.like(eq(exprString), eq("%100%"))).thenReturn(predicate);

            when(clienteQuery.select(root)).thenReturn(clienteQuery);
            when(clienteQuery.where(any(Predicate[].class))).thenReturn(clienteQuery);
            when(entityManager.createQuery(clienteQuery)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.setMaxResults(10)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.setFirstResult(0)).thenReturn(clienteTypedQuery);
            when(clienteTypedQuery.getResultList()).thenReturn(List.of(sampleCliente));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprString), eq("%100%"));
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            ClienteListRequest params = new ClienteListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Cliente.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(countQuery.orderBy(order)).thenReturn(countQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(20L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(20L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
