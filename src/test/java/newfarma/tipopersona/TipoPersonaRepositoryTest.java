package newfarma.tipopersona;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.TipoPersona;
import newfarma.tipopersona.dto.TipoPersonaListRequest;
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
@DisplayName("Pruebas Unitarias - TipoPersonaRepository")
class TipoPersonaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<TipoPersona> tipoPersonaQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<TipoPersona> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<TipoPersona> tipoPersonaTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private TipoPersonaRepository repository;

    private TipoPersona sampleTipo;

    @BeforeEach
    void setUp() {
        sampleTipo = new TipoPersona();
        sampleTipo.setIdtipopersona(1L);
        sampleTipo.setNombre("Cliente");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            TipoPersonaListRequest params = new TipoPersonaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(TipoPersona.class)).thenReturn(tipoPersonaQuery);
            when(tipoPersonaQuery.from(TipoPersona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(tipoPersonaQuery.select(root)).thenReturn(tipoPersonaQuery);
            when(tipoPersonaQuery.where(any(Predicate[].class))).thenReturn(tipoPersonaQuery);
            when(entityManager.createQuery(tipoPersonaQuery)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.setMaxResults(10)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.setFirstResult(0)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.getResultList()).thenReturn(List.of(sampleTipo));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleTipo);
            verify(tipoPersonaTypedQuery).setMaxResults(10);
            verify(tipoPersonaTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            TipoPersonaListRequest params = new TipoPersonaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("cli");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(TipoPersona.class)).thenReturn(tipoPersonaQuery);
            when(tipoPersonaQuery.from(TipoPersona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombre")).thenReturn((Path) pathString);
            when(builder.like(eq(pathString), eq("%CLI%"))).thenReturn(predicate);

            when(tipoPersonaQuery.select(root)).thenReturn(tipoPersonaQuery);
            when(tipoPersonaQuery.where(any(Predicate[].class))).thenReturn(tipoPersonaQuery);
            when(entityManager.createQuery(tipoPersonaQuery)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.setMaxResults(10)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.setFirstResult(0)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.getResultList()).thenReturn(List.of(sampleTipo));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(pathString), eq("%CLI%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombre' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            TipoPersonaListRequest params = new TipoPersonaListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombre("Cliente");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(TipoPersona.class)).thenReturn(tipoPersonaQuery);
            when(tipoPersonaQuery.from(TipoPersona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombre")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Cliente")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(tipoPersonaQuery.select(root)).thenReturn(tipoPersonaQuery);
            when(tipoPersonaQuery.where(any(Predicate[].class))).thenReturn(tipoPersonaQuery);
            when(entityManager.createQuery(tipoPersonaQuery)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.setMaxResults(5)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.setFirstResult(0)).thenReturn(tipoPersonaTypedQuery);
            when(tipoPersonaTypedQuery.getResultList()).thenReturn(List.of(sampleTipo));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Cliente");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            TipoPersonaListRequest params = new TipoPersonaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(TipoPersona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(5L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(5L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
