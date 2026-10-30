package newfarma.detalleventa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.model.detalleventa;
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
@DisplayName("Pruebas Unitarias - DetalleVentaRepository")
class DetalleVentaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<detalleventa> detalleQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<detalleventa> root;

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
    private TypedQuery<detalleventa> detalleTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private DetalleVentaRepository repository;

    private detalleventa sampleDetalle;

    @BeforeEach
    void setUp() {
        sampleDetalle = new detalleventa();
        sampleDetalle.setIddetalleventa(1L);
        sampleDetalle.setCodigodetalleventa("DV-001");
        sampleDetalle.setUnidades(3);
        sampleDetalle.setCostounidad(15.0);
        sampleDetalle.setTotal(45.0);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            DetalleVentaListRequest params = new DetalleVentaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(detalleventa.class)).thenReturn(detalleQuery);
            when(detalleQuery.from(detalleventa.class)).thenReturn(root);
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
            DetalleVentaListRequest params = new DetalleVentaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("dv-001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(detalleventa.class)).thenReturn(detalleQuery);
            when(detalleQuery.from(detalleventa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("codigodetalleventa")).thenReturn((Path) pathString);
            when(builder.upper(pathString)).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%DV-001%"))).thenReturn(predicate);

            when(detalleQuery.select(root)).thenReturn(detalleQuery);
            when(detalleQuery.where(any(Predicate[].class))).thenReturn(detalleQuery);
            when(entityManager.createQuery(detalleQuery)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setMaxResults(10)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setFirstResult(0)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.getResultList()).thenReturn(List.of(sampleDetalle));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%DV-001%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'codigodetalleventa' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            DetalleVentaListRequest params = new DetalleVentaListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setCodigodetalleventa("DV-001");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(detalleventa.class)).thenReturn(detalleQuery);
            when(detalleQuery.from(detalleventa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("codigodetalleventa")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "DV-001")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(detalleQuery.select(root)).thenReturn(detalleQuery);
            when(detalleQuery.where(any(Predicate[].class))).thenReturn(detalleQuery);
            when(entityManager.createQuery(detalleQuery)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setMaxResults(5)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.setFirstResult(0)).thenReturn(detalleTypedQuery);
            when(detalleTypedQuery.getResultList()).thenReturn(List.of(sampleDetalle));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "DV-001");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            DetalleVentaListRequest params = new DetalleVentaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(detalleventa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(60L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(60L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
