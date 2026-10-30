package newfarma.proveedor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Proveedor;
import newfarma.proveedor.dto.ProveedorListRequest;
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
@DisplayName("Pruebas Unitarias - ProveedorRepository")
class ProveedorRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Proveedor> proveedorQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Proveedor> root;

    @Mock
    private Path<Object> pathObject;

    @Mock
    private Expression<String> exprString;

    @Mock
    private Expression<Long> exprCount;

    @Mock
    private Order order;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<Proveedor> proveedorTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private ProveedorRepository repository;

    private Proveedor sampleProveedor;

    @BeforeEach
    void setUp() {
        sampleProveedor = new Proveedor();
        sampleProveedor.setIdproveedor(1L);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            ProveedorListRequest params = new ProveedorListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Proveedor.class)).thenReturn(proveedorQuery);
            when(proveedorQuery.from(Proveedor.class)).thenReturn(root);
            when(root.get("idproveedor")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(proveedorQuery.orderBy(order)).thenReturn(proveedorQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(proveedorQuery.select(root)).thenReturn(proveedorQuery);
            when(proveedorQuery.where(any(Predicate[].class))).thenReturn(proveedorQuery);
            when(entityManager.createQuery(proveedorQuery)).thenReturn(proveedorTypedQuery);
            when(proveedorTypedQuery.setMaxResults(10)).thenReturn(proveedorTypedQuery);
            when(proveedorTypedQuery.setFirstResult(0)).thenReturn(proveedorTypedQuery);
            when(proveedorTypedQuery.getResultList()).thenReturn(List.of(sampleProveedor));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleProveedor);
            verify(proveedorTypedQuery).setMaxResults(10);
            verify(proveedorTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica like sobre idproveedor")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplyLikeCriteria() {
            ProveedorListRequest params = new ProveedorListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("20");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Proveedor.class)).thenReturn(proveedorQuery);
            when(proveedorQuery.from(Proveedor.class)).thenReturn(root);
            when(root.get("idproveedor")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(proveedorQuery.orderBy(order)).thenReturn(proveedorQuery);

            when(builder.conjunction()).thenReturn(predicate);

            when(pathObject.as(String.class)).thenReturn(exprString);
            when(builder.like(eq(exprString), eq("%20%"))).thenReturn(predicate);

            when(proveedorQuery.select(root)).thenReturn(proveedorQuery);
            when(proveedorQuery.where(any(Predicate[].class))).thenReturn(proveedorQuery);
            when(entityManager.createQuery(proveedorQuery)).thenReturn(proveedorTypedQuery);
            when(proveedorTypedQuery.setMaxResults(10)).thenReturn(proveedorTypedQuery);
            when(proveedorTypedQuery.setFirstResult(0)).thenReturn(proveedorTypedQuery);
            when(proveedorTypedQuery.getResultList()).thenReturn(List.of(sampleProveedor));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprString), eq("%20%"));
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total")
        void list_ModeT_ShouldReturnCount() {
            ProveedorListRequest params = new ProveedorListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Proveedor.class)).thenReturn(root);
            when(root.get("idproveedor")).thenReturn((Path) pathObject);
            when(builder.desc(pathObject)).thenReturn(order);
            when(countQuery.orderBy(order)).thenReturn(countQuery);

            when(builder.conjunction()).thenReturn(predicate);
            when(builder.count(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(12L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(12L);
            verify(builder).count(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
