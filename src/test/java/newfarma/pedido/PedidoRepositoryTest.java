package newfarma.pedido;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Pedido;
import newfarma.pedido.dto.PedidoListRequest;
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
@DisplayName("Pruebas Unitarias - PedidoRepository")
class PedidoRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Pedido> pedidoQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Pedido> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Expression<String> exprString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Pedido> pedidoTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private PedidoRepository repository;

    private Pedido samplePedido;

    @BeforeEach
    void setUp() {
        samplePedido = new Pedido();
        samplePedido.setIdpedido(1L);
        samplePedido.setCodigopedido("PED-001");
        samplePedido.setStock(10);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            PedidoListRequest params = new PedidoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Pedido.class)).thenReturn(pedidoQuery);
            when(pedidoQuery.from(Pedido.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(pedidoQuery.select(root)).thenReturn(pedidoQuery);
            when(pedidoQuery.where(any(Predicate[].class))).thenReturn(pedidoQuery);
            when(entityManager.createQuery(pedidoQuery)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.setMaxResults(10)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.setFirstResult(0)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.getResultList()).thenReturn(List.of(samplePedido));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(samplePedido);
            verify(pedidoTypedQuery).setMaxResults(10);
            verify(pedidoTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like con cast a String")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            PedidoListRequest params = new PedidoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("100");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Pedido.class)).thenReturn(pedidoQuery);
            when(pedidoQuery.from(Pedido.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("idpedido")).thenReturn((Path) pathObject);
            when(pathObject.as(String.class)).thenReturn(exprString);
            when(builder.like(eq(exprString), eq("%100%"))).thenReturn(predicate);

            when(pedidoQuery.select(root)).thenReturn(pedidoQuery);
            when(pedidoQuery.where(any(Predicate[].class))).thenReturn(pedidoQuery);
            when(entityManager.createQuery(pedidoQuery)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.setMaxResults(10)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.setFirstResult(0)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.getResultList()).thenReturn(List.of(samplePedido));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprString), eq("%100%"));
        }

        @Test
        @DisplayName("list con mode 'L' cuando no hay resultados retorna lista vacía")
        void list_ModeL_WhenNoResults_ShouldReturnEmptyList() {
            PedidoListRequest params = new PedidoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Pedido.class)).thenReturn(pedidoQuery);
            when(pedidoQuery.from(Pedido.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(pedidoQuery.select(root)).thenReturn(pedidoQuery);
            when(pedidoQuery.where(any(Predicate[].class))).thenReturn(pedidoQuery);
            when(entityManager.createQuery(pedidoQuery)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.setMaxResults(10)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.setFirstResult(0)).thenReturn(pedidoTypedQuery);
            when(pedidoTypedQuery.getResultList()).thenReturn(List.of());

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).isEmpty();
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            PedidoListRequest params = new PedidoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Pedido.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(15L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(15L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
