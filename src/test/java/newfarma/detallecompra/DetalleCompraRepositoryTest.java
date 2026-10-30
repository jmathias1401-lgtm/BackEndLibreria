package newfarma.detallecompra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.model.detallecompra;
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
@DisplayName("Pruebas Unitarias - DetalleCompraRepository")
class DetalleCompraRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<detallecompra> detalleQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<detallecompra> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<String> exprUpper;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<detallecompra> detalleTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private DetalleCompraRepository repository;

    private detallecompra sampleDetalle;

    @BeforeEach
    void setUp() {
        sampleDetalle = new detallecompra();
        sampleDetalle.setIddetallecompra(1L);
        sampleDetalle.setCodigodetallecompra("DC-001");
        sampleDetalle.setUnidades(10);
        sampleDetalle.setCostounidad(5.0);
        sampleDetalle.setTotal(50.0);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            DetalleCompraListRequest params = new DetalleCompraListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(detallecompra.class)).thenReturn(detalleQuery);
            when(detalleQuery.from(detallecompra.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(detalleQuery.select(root)).thenReturn(detalleQuery);
            when(detalleQuery.where(any(Predicate[].class))).thenReturn(detalleQuery);
            when(entityManager.createQuery(detalleQuery)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setMaxResults(10)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setFirstResult(0)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.getResultList()).thenReturn(List.of(sampleDetalle));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleDetalle);
            verify(detalleTypedQuery).setMaxResults(10);
            verify(detalleTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica upper y like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            DetalleCompraListRequest params = new DetalleCompraListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("dc-001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(detallecompra.class)).thenReturn(detalleQuery);
            when(detalleQuery.from(detallecompra.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("codigodetallecompra")).thenReturn((Path) pathString);
            when(builder.upper(pathString)).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%DC-001%"))).thenReturn(predicate);

            when(detalleQuery.select(root)).thenReturn(detalleQuery);
            when(detalleQuery.where(any(Predicate[].class))).thenReturn(detalleQuery);
            when(entityManager.createQuery(detalleQuery)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setMaxResults(10)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setFirstResult(0)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.getResultList()).thenReturn(List.of(sampleDetalle));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%DC-001%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'codigodetallecompra' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            DetalleCompraListRequest params = new DetalleCompraListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setCodigodetallecompra("DC-001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(detallecompra.class)).thenReturn(detalleQuery);
            when(detalleQuery.from(detallecompra.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("codigodetallecompra")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "DC-001")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(detalleQuery.select(root)).thenReturn(detalleQuery);
            when(detalleQuery.where(any(Predicate[].class))).thenReturn(detalleQuery);
            when(entityManager.createQuery(detalleQuery)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setMaxResults(5)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setFirstResult(0)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.getResultList()).thenReturn(List.of(sampleDetalle));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "DC-001");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            DetalleCompraListRequest params = new DetalleCompraListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(detallecompra.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(40L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(40L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
