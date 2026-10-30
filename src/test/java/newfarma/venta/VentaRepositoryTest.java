package newfarma.venta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Cliente;
import newfarma.model.Persona;
import newfarma.model.Venta;
import newfarma.venta.dto.VentaListRequest;
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
@DisplayName("Pruebas Unitarias - VentaRepository")
class VentaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Venta> ventaQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Venta> root;

    @Mock
    @SuppressWarnings("rawtypes")
    private Join clienteJoin;

    @Mock
    @SuppressWarnings("rawtypes")
    private Join personaJoin;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Order order;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Venta> ventaTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private ventaRepository repository;

    private Venta sampleVenta;

    @BeforeEach
    void setUp() {
        sampleVenta = new Venta();
        sampleVenta.setIdventa(1L);
        sampleVenta.setSerie("B001");
        sampleVenta.setCorrelativo("000045");
        sampleVenta.setCostoventa(45.0);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            VentaListRequest params = new VentaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Venta.class)).thenReturn(ventaQuery);
            when(ventaQuery.from(Venta.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(ventaQuery.orderBy(order)).thenReturn(ventaQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(ventaQuery.select(root)).thenReturn(ventaQuery);
            when(ventaQuery.where(any(Predicate[].class))).thenReturn(ventaQuery);
            when(entityManager.createQuery(ventaQuery)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.setMaxResults(10)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.setFirstResult(0)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.getResultList()).thenReturn(List.of(sampleVenta));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleVenta);
            verify(ventaTypedQuery).setMaxResults(10);
            verify(ventaTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' realiza joins con cliente y persona")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyJoinsAndNombreLike() {
            VentaListRequest params = new VentaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("carlos");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Venta.class)).thenReturn(ventaQuery);
            when(ventaQuery.from(Venta.class)).thenReturn(root);
            doReturn(clienteJoin).when(root).join("cliente", JoinType.INNER);
            doReturn(personaJoin).when(clienteJoin).join("persona");

            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(ventaQuery.orderBy(order)).thenReturn(ventaQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(personaJoin.get("nombre")).thenReturn((Path) pathString);
            when(builder.like(eq(pathString), eq("%carlos%"))).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(ventaQuery.select(root)).thenReturn(ventaQuery);
            when(ventaQuery.where(any(Predicate[].class))).thenReturn(ventaQuery);
            when(entityManager.createQuery(ventaQuery)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.setMaxResults(10)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.setFirstResult(0)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.getResultList()).thenReturn(List.of(sampleVenta));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(root).join("cliente", JoinType.INNER);
            verify(clienteJoin).join("persona");
            verify(builder).like(eq(pathString), eq("%carlos%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de serie aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            VentaListRequest params = new VentaListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setSerie("B001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Venta.class)).thenReturn(ventaQuery);
            when(ventaQuery.from(Venta.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(ventaQuery.orderBy(order)).thenReturn(ventaQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("serie")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "B001")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(ventaQuery.select(root)).thenReturn(ventaQuery);
            when(ventaQuery.where(any(Predicate[].class))).thenReturn(ventaQuery);
            when(entityManager.createQuery(ventaQuery)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.setMaxResults(5)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.setFirstResult(0)).thenReturn(ventaTypedQuery);
            when(ventaTypedQuery.getResultList()).thenReturn(List.of(sampleVenta));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "B001");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            VentaListRequest params = new VentaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Venta.class)).thenReturn(root);
            when(root.get("id")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(countQuery.orderBy(order)).thenReturn(countQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(35L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(35L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
