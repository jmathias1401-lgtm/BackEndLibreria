package newfarma.empresa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.model.empresa;
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
@DisplayName("Pruebas Unitarias - EmpresaRepository")
class EmpresaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<empresa> empresaQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<empresa> root;

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
    private Predicate predicate;

    @Mock
    private TypedQuery<empresa> empresaTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private EmpresaRepository repository;

    private empresa sampleEmpresa;

    @BeforeEach
    void setUp() {
        sampleEmpresa = new empresa();
        sampleEmpresa.setIdempresa(1L);
        sampleEmpresa.setNombre("Farma Salud SAC");
        sampleEmpresa.setDireccion("Av. Principal 123");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            EmpresaListRequest params = new EmpresaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(empresa.class)).thenReturn(empresaQuery);
            when(empresaQuery.from(empresa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(empresaQuery.select(root)).thenReturn(empresaQuery);
            when(empresaQuery.where(any(Predicate[].class))).thenReturn(empresaQuery);
            when(entityManager.createQuery(empresaQuery)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.setMaxResults(10)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.setFirstResult(0)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.getResultList()).thenReturn(List.of(sampleEmpresa));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleEmpresa);
            verify(empresaTypedQuery).setMaxResults(10);
            verify(empresaTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica concatenación y like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplySearchCriteria() {
            EmpresaListRequest params = new EmpresaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("farma");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(empresa.class)).thenReturn(empresaQuery);
            when(empresaQuery.from(empresa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get(anyString())).thenReturn((Path) pathString);
            when(builder.concat(any(Expression.class), any(Expression.class))).thenReturn(exprConcat);
            when(builder.upper(any(Expression.class))).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%FARMA%"))).thenReturn(predicate);

            when(empresaQuery.select(root)).thenReturn(empresaQuery);
            when(empresaQuery.where(any(Predicate[].class))).thenReturn(empresaQuery);
            when(entityManager.createQuery(empresaQuery)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.setMaxResults(10)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.setFirstResult(0)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.getResultList()).thenReturn(List.of(sampleEmpresa));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%FARMA%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombre' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            EmpresaListRequest params = new EmpresaListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombre("Farma Salud SAC");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(empresa.class)).thenReturn(empresaQuery);
            when(empresaQuery.from(empresa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombre")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Farma Salud SAC")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(empresaQuery.select(root)).thenReturn(empresaQuery);
            when(empresaQuery.where(any(Predicate[].class))).thenReturn(empresaQuery);
            when(entityManager.createQuery(empresaQuery)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.setMaxResults(5)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.setFirstResult(0)).thenReturn(empresaTypedQuery);
            when(empresaTypedQuery.getResultList()).thenReturn(List.of(sampleEmpresa));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Farma Salud SAC");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            EmpresaListRequest params = new EmpresaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(empresa.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(3L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(3L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
