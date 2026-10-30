package newfarma.compra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.compra.dto.CompraListRequest;
import newfarma.model.Compra;
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
@DisplayName("Pruebas Unitarias - CompraRepository")
class CompraRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Compra> compraQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Compra> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<String> exprConcat;

    @Mock
    private Expression<String> exprUpper;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Order order;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Compra> compraTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private compraRepository repository;

    private Compra sampleCompra;

    @BeforeEach
    void setUp() {
        sampleCompra = new Compra();
        sampleCompra.setIdcompra(1L);
        sampleCompra.setSerie("F001");
        sampleCompra.setCorrelativo("000123");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada ordenada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            CompraListRequest params = new CompraListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Compra.class)).thenReturn(compraQuery);
            when(compraQuery.from(Compra.class)).thenReturn(root);
            when(root.get("idcompra")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(compraQuery.orderBy(order)).thenReturn(compraQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(compraQuery.select(root)).thenReturn(compraQuery);
            when(compraQuery.where(any(Predicate[].class))).thenReturn(compraQuery);
            when(entityManager.createQuery(compraQuery)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.setMaxResults(10)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.setFirstResult(0)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.getResultList()).thenReturn(List.of(sampleCompra));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleCompra);
            verify(compraTypedQuery).setMaxResults(10);
            verify(compraTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'serie' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            CompraListRequest params = new CompraListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setSerie("F001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Compra.class)).thenReturn(compraQuery);
            when(compraQuery.from(Compra.class)).thenReturn(root);
            when(root.get("idcompra")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(compraQuery.orderBy(order)).thenReturn(compraQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("serie")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "F001")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(compraQuery.select(root)).thenReturn(compraQuery);
            when(compraQuery.where(any(Predicate[].class))).thenReturn(compraQuery);
            when(entityManager.createQuery(compraQuery)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.setMaxResults(5)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.setFirstResult(0)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.getResultList()).thenReturn(List.of(sampleCompra));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "F001");
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica concatenación múltiple y like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplySearchCriteria() {
            CompraListRequest params = new CompraListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("f001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Compra.class)).thenReturn(compraQuery);
            when(compraQuery.from(Compra.class)).thenReturn(root);
            when(root.get(anyString())).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(compraQuery.orderBy(order)).thenReturn(compraQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(pathObject.get(anyString())).thenReturn((Path) pathObject);
            when(builder.concat(any(Expression.class), any(Expression.class))).thenReturn(exprConcat);
            when(builder.concat(any(Expression.class), anyString())).thenReturn(exprConcat);
            when(builder.upper(any())).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%F001%"))).thenReturn(predicate);

            when(compraQuery.select(root)).thenReturn(compraQuery);
            when(compraQuery.where(any(Predicate[].class))).thenReturn(compraQuery);
            when(entityManager.createQuery(compraQuery)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.setMaxResults(10)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.setFirstResult(0)).thenReturn(compraTypedQuery);
            when(compraTypedQuery.getResultList()).thenReturn(List.of(sampleCompra));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%F001%"));
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            CompraListRequest params = new CompraListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Compra.class)).thenReturn(root);
            when(root.get("idcompra")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(countQuery.orderBy(order)).thenReturn(countQuery);

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
