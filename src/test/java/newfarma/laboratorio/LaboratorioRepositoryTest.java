package newfarma.laboratorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.model.Laboratorio;
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
@DisplayName("Pruebas Unitarias - LaboratorioRepository")
class LaboratorioRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Laboratorio> laboratorioQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Laboratorio> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Path<String> pathString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Laboratorio> laboratorioTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private LaboratorioRepository repository;

    private Laboratorio sampleLab;

    @BeforeEach
    void setUp() {
        sampleLab = new Laboratorio();
        sampleLab.setIdlaboratorio(1L);
        sampleLab.setNombrelaboratorio("Bayer");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            LaboratorioListRequest params = new LaboratorioListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Laboratorio.class)).thenReturn(laboratorioQuery);
            when(laboratorioQuery.from(Laboratorio.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(laboratorioQuery.select(root)).thenReturn(laboratorioQuery);
            when(laboratorioQuery.where(any(Predicate[].class))).thenReturn(laboratorioQuery);
            when(entityManager.createQuery(laboratorioQuery)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.setMaxResults(10)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.setFirstResult(0)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.getResultList()).thenReturn(List.of(sampleLab));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleLab);
            verify(laboratorioTypedQuery).setMaxResults(10);
            verify(laboratorioTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            LaboratorioListRequest params = new LaboratorioListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("bay");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Laboratorio.class)).thenReturn(laboratorioQuery);
            when(laboratorioQuery.from(Laboratorio.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombrelaboratorio")).thenReturn((Path) pathString);
            when(builder.like(eq(pathString), eq("%BAY%"))).thenReturn(predicate);

            when(laboratorioQuery.select(root)).thenReturn(laboratorioQuery);
            when(laboratorioQuery.where(any(Predicate[].class))).thenReturn(laboratorioQuery);
            when(entityManager.createQuery(laboratorioQuery)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.setMaxResults(10)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.setFirstResult(0)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.getResultList()).thenReturn(List.of(sampleLab));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(pathString), eq("%BAY%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombrelaboratorio' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            LaboratorioListRequest params = new LaboratorioListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombrelaboratorio("Bayer");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Laboratorio.class)).thenReturn(laboratorioQuery);
            when(laboratorioQuery.from(Laboratorio.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombrelaboratorio")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Bayer")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(laboratorioQuery.select(root)).thenReturn(laboratorioQuery);
            when(laboratorioQuery.where(any(Predicate[].class))).thenReturn(laboratorioQuery);
            when(entityManager.createQuery(laboratorioQuery)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.setMaxResults(5)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.setFirstResult(0)).thenReturn(laboratorioTypedQuery);
            when(laboratorioTypedQuery.getResultList()).thenReturn(List.of(sampleLab));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Bayer");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            LaboratorioListRequest params = new LaboratorioListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Laboratorio.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(18L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(18L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
