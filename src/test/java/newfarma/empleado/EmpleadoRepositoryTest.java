package newfarma.empleado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.model.Empleado;
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
@DisplayName("Pruebas Unitarias - EmpleadoRepository")
class EmpleadoRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Empleado> empleadoQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Empleado> root;

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
    private TypedQuery<Empleado> empleadoTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private EmpleadoRepository repository;

    private Empleado sampleEmpleado;

    @BeforeEach
    void setUp() {
        sampleEmpleado = new Empleado();
        sampleEmpleado.setIdempleado(1L);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            EmpleadoListRequest params = new EmpleadoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Empleado.class)).thenReturn(empleadoQuery);
            when(empleadoQuery.from(Empleado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(empleadoQuery.select(root)).thenReturn(empleadoQuery);
            when(empleadoQuery.where(any(Predicate[].class))).thenReturn(empleadoQuery);
            when(entityManager.createQuery(empleadoQuery)).thenReturn(empleadoTypedQuery);
            when(empleadoTypedQuery.setMaxResults(10)).thenReturn(empleadoTypedQuery);
            when(empleadoTypedQuery.setFirstResult(0)).thenReturn(empleadoTypedQuery);
            when(empleadoTypedQuery.getResultList()).thenReturn(List.of(sampleEmpleado));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleEmpleado);
            verify(empleadoTypedQuery).setMaxResults(10);
            verify(empleadoTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' busca por nombre de persona")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyPersonaNombreLike() {
            EmpleadoListRequest params = new EmpleadoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("maria");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Empleado.class)).thenReturn(empleadoQuery);
            when(empleadoQuery.from(Empleado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("persona")).thenReturn((Path) pathObject);
            when(pathObject.get("nombre")).thenReturn((Path) pathString);
            when(builder.upper(pathString)).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%MARIA%"))).thenReturn(predicate);

            when(empleadoQuery.select(root)).thenReturn(empleadoQuery);
            when(empleadoQuery.where(any(Predicate[].class))).thenReturn(empleadoQuery);
            when(entityManager.createQuery(empleadoQuery)).thenReturn(empleadoTypedQuery);
            when(empleadoTypedQuery.setMaxResults(10)).thenReturn(empleadoTypedQuery);
            when(empleadoTypedQuery.setFirstResult(0)).thenReturn(empleadoTypedQuery);
            when(empleadoTypedQuery.getResultList()).thenReturn(List.of(sampleEmpleado));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%MARIA%"));
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            EmpleadoListRequest params = new EmpleadoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Empleado.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(8L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(8L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
