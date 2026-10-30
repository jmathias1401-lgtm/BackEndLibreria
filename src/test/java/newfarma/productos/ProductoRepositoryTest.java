package newfarma.productos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
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
@DisplayName("Pruebas Unitarias - ProductoRepository")
class ProductoRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Producto> productoQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Producto> root;

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
    private TypedQuery<Producto> productoTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private ProductoRepository repository;

    private Producto sampleProducto;

    @BeforeEach
    void setUp() {
        sampleProducto = new Producto();
        sampleProducto.setIdproducto(1L);
        sampleProducto.setNombre("Paracetamol 500mg");
        sampleProducto.setComposicion("Paracetamol");
        sampleProducto.setPrecioventa(1.50);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            ProductListRequest params = new ProductListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Producto.class)).thenReturn(productoQuery);
            when(productoQuery.from(Producto.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(productoQuery.select(root)).thenReturn(productoQuery);
            when(productoQuery.where(any(Predicate[].class))).thenReturn(productoQuery);
            when(entityManager.createQuery(productoQuery)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.setMaxResults(10)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.setFirstResult(0)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.getResultList()).thenReturn(List.of(sampleProducto));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(sampleProducto);
            verify(productoTypedQuery).setMaxResults(10);
            verify(productoTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica concatenación y like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplySearchCriteria() {
            ProductListRequest params = new ProductListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("paracetamol");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Producto.class)).thenReturn(productoQuery);
            when(productoQuery.from(Producto.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get(anyString())).thenReturn((Path) pathString);
            when(builder.concat(any(Expression.class), any(Expression.class))).thenReturn(exprConcat);
            when(builder.upper(any(Expression.class))).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%PARACETAMOL%"))).thenReturn(predicate);

            when(productoQuery.select(root)).thenReturn(productoQuery);
            when(productoQuery.where(any(Predicate[].class))).thenReturn(productoQuery);
            when(entityManager.createQuery(productoQuery)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.setMaxResults(10)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.setFirstResult(0)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.getResultList()).thenReturn(List.of(sampleProducto));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%PARACETAMOL%"));
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombre' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            ProductListRequest params = new ProductListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombre("Paracetamol 500mg");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Producto.class)).thenReturn(productoQuery);
            when(productoQuery.from(Producto.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombre")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Paracetamol 500mg")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(productoQuery.select(root)).thenReturn(productoQuery);
            when(productoQuery.where(any(Predicate[].class))).thenReturn(productoQuery);
            when(entityManager.createQuery(productoQuery)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.setMaxResults(5)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.setFirstResult(0)).thenReturn(productoTypedQuery);
            when(productoTypedQuery.getResultList()).thenReturn(List.of(sampleProducto));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Paracetamol 500mg");
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            ProductListRequest params = new ProductListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Producto.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(100L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(100L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
