package newfarma.presentacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Presentacion;
import newfarma.presentacion.dto.PresentacionListRequest;
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
@DisplayName("Pruebas Unitarias - PresentacionRepository")
class PresentacionRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Presentacion> presentacionQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Presentacion> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Presentacion> presentacionTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private PresentacionRepository repository;

    private Presentacion samplePresentacion;

    @BeforeEach
    void setUp() {
        samplePresentacion = new Presentacion();
        samplePresentacion.setIdpresentacion(1L);
        samplePresentacion.setNombrepresentacion("Frasco 100ml");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            PresentacionListRequest params = new PresentacionListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Presentacion.class)).thenReturn(presentacionQuery);
            when(presentacionQuery.from(Presentacion.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(presentacionQuery.select(root)).thenReturn(presentacionQuery);
            when(presentacionQuery.where(any(Predicate[].class))).thenReturn(presentacionQuery);
            when(entityManager.createQuery(presentacionQuery)).thenReturn(presentacionTypedQuery);
            when(presentacionTypedQuery.setMaxResults(10)).thenReturn(presentacionTypedQuery);
            when(presentacionTypedQuery.setFirstResult(0)).thenReturn(presentacionTypedQuery);
            when(presentacionTypedQuery.getResultList()).thenReturn(List.of(samplePresentacion));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(samplePresentacion);
            verify(presentacionTypedQuery).setMaxResults(10);
            verify(presentacionTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombre' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            PresentacionListRequest params = new PresentacionListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombre("Frasco 100ml");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Presentacion.class)).thenReturn(presentacionQuery);
            when(presentacionQuery.from(Presentacion.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombre")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Frasco 100ml")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(presentacionQuery.select(root)).thenReturn(presentacionQuery);
            when(presentacionQuery.where(any(Predicate[].class))).thenReturn(presentacionQuery);
            when(entityManager.createQuery(presentacionQuery)).thenReturn(presentacionTypedQuery);
            when(presentacionTypedQuery.setMaxResults(5)).thenReturn(presentacionTypedQuery);
            when(presentacionTypedQuery.setFirstResult(0)).thenReturn(presentacionTypedQuery);
            when(presentacionTypedQuery.getResultList()).thenReturn(List.of(samplePresentacion));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Frasco 100ml");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            PresentacionListRequest params = new PresentacionListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Presentacion.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(14L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(14L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
