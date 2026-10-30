package newfarma.cargo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.cargo.dto.CargoListRequest;
import newfarma.model.Cargo;
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
@DisplayName("Pruebas Unitarias - CargoRepository")
class CargoRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Cargo> cargoQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Cargo> root;

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
    private TypedQuery<Cargo> cargoTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private CargoRepository repository;

    private Cargo sampleCargo;

    @BeforeEach
    void setUp() {
        sampleCargo = new Cargo();
        sampleCargo.setIdcargo(1L);
        sampleCargo.setNombrecargo("Farmacéutico");
        sampleCargo.setDescripcion("Encargado de dispensación");
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            CargoListRequest params = new CargoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Cargo.class)).thenReturn(cargoQuery);
            when(cargoQuery.from(Cargo.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(cargoQuery.select(root)).thenReturn(cargoQuery);
            when(cargoQuery.where(any(Predicate[].class))).thenReturn(cargoQuery);
            when(entityManager.createQuery(cargoQuery)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.setMaxResults(10)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.setFirstResult(0)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.getResultList()).thenReturn(List.of(sampleCargo));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleCargo);
            verify(cargoTypedQuery).setMaxResults(10);
            verify(cargoTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            CargoListRequest params = new CargoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("farma");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Cargo.class)).thenReturn(cargoQuery);
            when(cargoQuery.from(Cargo.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombrecargo")).thenReturn((Path) pathString);
            when(builder.like(eq(pathString), eq("%FARMA%"))).thenReturn(predicate);

            when(cargoQuery.select(root)).thenReturn(cargoQuery);
            when(cargoQuery.where(any(Predicate[].class))).thenReturn(cargoQuery);
            when(entityManager.createQuery(cargoQuery)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.setMaxResults(10)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.setFirstResult(0)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.getResultList()).thenReturn(List.of(sampleCargo));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(pathString), eq("%FARMA%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombrecargo' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            CargoListRequest params = new CargoListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombrecargo("Farmacéutico");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Cargo.class)).thenReturn(cargoQuery);
            when(cargoQuery.from(Cargo.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombrecargo")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Farmacéutico")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(cargoQuery.select(root)).thenReturn(cargoQuery);
            when(cargoQuery.where(any(Predicate[].class))).thenReturn(cargoQuery);
            when(entityManager.createQuery(cargoQuery)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.setMaxResults(5)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.setFirstResult(0)).thenReturn(cargoTypedQuery);
            when(cargoTypedQuery.getResultList()).thenReturn(List.of(sampleCargo));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Farmacéutico");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            CargoListRequest params = new CargoListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Cargo.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(15L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(15L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
