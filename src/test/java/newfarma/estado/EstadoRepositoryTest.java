package newfarma.estado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.estado.dto.EstadoListRequest;
import newfarma.model.Estado;
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
@DisplayName("Pruebas Unitarias - EstadoRepository")
class EstadoRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Estado> estadoQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Estado> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Estado> estadoTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private EstadoRepository repository;

    private Estado sampleEstado;

    @BeforeEach
    void setUp() {
        sampleEstado = new Estado();
        sampleEstado.setIdestado(1L);
        sampleEstado.setEstado("Activo");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            EstadoListRequest params = new EstadoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Estado.class)).thenReturn(estadoQuery);
            when(estadoQuery.from(Estado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(estadoQuery.select(root)).thenReturn(estadoQuery);
            when(estadoQuery.where(any(Predicate[].class))).thenReturn(estadoQuery);
            when(entityManager.createQuery(estadoQuery)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.setMaxResults(10)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.setFirstResult(0)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.getResultList()).thenReturn(List.of(sampleEstado));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleEstado);
            verify(estadoTypedQuery).setMaxResults(10);
            verify(estadoTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            EstadoListRequest params = new EstadoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("act");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Estado.class)).thenReturn(estadoQuery);
            when(estadoQuery.from(Estado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("estado")).thenReturn((Path) pathString);
            when(builder.like(eq(pathString), eq("%ACT%"))).thenReturn(predicate);

            when(estadoQuery.select(root)).thenReturn(estadoQuery);
            when(estadoQuery.where(any(Predicate[].class))).thenReturn(estadoQuery);
            when(entityManager.createQuery(estadoQuery)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.setMaxResults(10)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.setFirstResult(0)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.getResultList()).thenReturn(List.of(sampleEstado));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(pathString), eq("%ACT%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'estado' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            EstadoListRequest params = new EstadoListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setEstado("Activo");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Estado.class)).thenReturn(estadoQuery);
            when(estadoQuery.from(Estado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("estado")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Activo")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(estadoQuery.select(root)).thenReturn(estadoQuery);
            when(estadoQuery.where(any(Predicate[].class))).thenReturn(estadoQuery);
            when(entityManager.createQuery(estadoQuery)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.setMaxResults(5)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.setFirstResult(0)).thenReturn(estadoTypedQuery);
            when(estadoTypedQuery.getResultList()).thenReturn(List.of(sampleEstado));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Activo");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            EstadoListRequest params = new EstadoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Estado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(2L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(2L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
