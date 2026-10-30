package newfarma.unidadmedida;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.UnidadMedida;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
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
@DisplayName("Pruebas Unitarias - UnidadMedidaRepository")
class UnidadMedidaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<UnidadMedida> unidadMedidaQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<UnidadMedida> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<UnidadMedida> unidadMedidaTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private UnidadMedidaRepository repository;

    private UnidadMedida sampleUnidad;

    @BeforeEach
    void setUp() {
        sampleUnidad = new UnidadMedida();
        sampleUnidad.setIdunidadmedida(1);
        sampleUnidad.setNombreunidad("Caja");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            UnidadMedidaListRequest params = new UnidadMedidaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(UnidadMedida.class)).thenReturn(unidadMedidaQuery);
            when(unidadMedidaQuery.from(UnidadMedida.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(unidadMedidaQuery.select(root)).thenReturn(unidadMedidaQuery);
            when(unidadMedidaQuery.where(any(Predicate[].class))).thenReturn(unidadMedidaQuery);
            when(entityManager.createQuery(unidadMedidaQuery)).thenReturn(unidadMedidaTypedQuery);
            when(unidadMedidaTypedQuery.setMaxResults(10)).thenReturn(unidadMedidaTypedQuery);
            when(unidadMedidaTypedQuery.setFirstResult(0)).thenReturn(unidadMedidaTypedQuery);
            when(unidadMedidaTypedQuery.getResultList()).thenReturn(List.of(sampleUnidad));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleUnidad);
            verify(unidadMedidaTypedQuery).setMaxResults(10);
            verify(unidadMedidaTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombreunidad' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            UnidadMedidaListRequest params = new UnidadMedidaListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombreunidad("Caja");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(UnidadMedida.class)).thenReturn(unidadMedidaQuery);
            when(unidadMedidaQuery.from(UnidadMedida.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombreunidad")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Caja")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(unidadMedidaQuery.select(root)).thenReturn(unidadMedidaQuery);
            when(unidadMedidaQuery.where(any(Predicate[].class))).thenReturn(unidadMedidaQuery);
            when(entityManager.createQuery(unidadMedidaQuery)).thenReturn(unidadMedidaTypedQuery);
            when(unidadMedidaTypedQuery.setMaxResults(5)).thenReturn(unidadMedidaTypedQuery);
            when(unidadMedidaTypedQuery.setFirstResult(0)).thenReturn(unidadMedidaTypedQuery);
            when(unidadMedidaTypedQuery.getResultList()).thenReturn(List.of(sampleUnidad));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Caja");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            UnidadMedidaListRequest params = new UnidadMedidaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(UnidadMedida.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(6L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(6L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
