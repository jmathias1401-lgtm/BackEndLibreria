package newfarma.persona;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import newfarma.model.Persona;
import newfarma.persona.dto.PersonaListRequest;
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
@DisplayName("Pruebas Unitarias - PersonaRepository")
class PersonaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery<Persona> personaQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Persona> root;

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
    private TypedQuery<Persona> personaTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private PersonaRepository repository;

    private Persona samplePersona;

    @BeforeEach
    void setUp() {
        samplePersona = new Persona();
        samplePersona.setIdpersona(1L);
        samplePersona.setNombre("Juan");
        samplePersona.setDni(12345678L);
    }

    @Nested
    @DisplayName("list() - Modo 'L' (Lista de Registros)")
    class ListModeTests {

        @Test
        @DisplayName("list con mode 'L' sin filtros retorna lista paginada")
        void list_ModeL_NoFilters_ShouldReturnList() {
            PersonaListRequest params = new PersonaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Persona.class)).thenReturn(personaQuery);
            when(personaQuery.from(Persona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(personaQuery.select(root)).thenReturn(personaQuery);
            when(personaQuery.where(any(Predicate[].class))).thenReturn(personaQuery);
            when(entityManager.createQuery(personaQuery)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.setMaxResults(10)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.setFirstResult(0)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.getResultList()).thenReturn(List.of(samplePersona));

            Object result = repository.list(params, "L");

            assertThat(result).isInstanceOf(List.class);
            List<?> list = (List<?>) result;
            assertThat(list).hasSize(1);
            assertThat(list.get(0)).isEqualTo(samplePersona);
            verify(personaTypedQuery).setMaxResults(10);
            verify(personaTypedQuery).setFirstResult(0);
        }

        @Test
        @DisplayName("list con mode 'L' y filtro de igualdad 'nombre' aplica eq")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithEqFilter_ShouldApplyEqCriteria() {
            PersonaListRequest params = new PersonaListRequest();
            params.setPage(1);
            params.setXpage(5);
            params.setOffset(0);
            params.setNombre("Juan");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Persona.class)).thenReturn(personaQuery);
            when(personaQuery.from(Persona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get("nombre")).thenReturn((Path) pathObject);
            when(builder.equal(pathObject, "Juan")).thenReturn(predicate);
            when(builder.and(any(), any())).thenReturn(predicate);

            when(personaQuery.select(root)).thenReturn(personaQuery);
            when(personaQuery.where(any(Predicate[].class))).thenReturn(personaQuery);
            when(entityManager.createQuery(personaQuery)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.setMaxResults(5)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.setFirstResult(0)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.getResultList()).thenReturn(List.of(samplePersona));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).equal(pathObject, "Juan");
        }

        @Test
        @DisplayName("list con mode 'L' y filtro 'search' aplica concatenación y like")
        @SuppressWarnings("unchecked")
        void list_ModeL_WithSearch_ShouldApplySearchCriteria() {
            PersonaListRequest params = new PersonaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);
            params.setSearch("juan");

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Persona.class)).thenReturn(personaQuery);
            when(personaQuery.from(Persona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);

            when(root.get(anyString())).thenReturn((Path) pathString);
            when(builder.concat(any(Expression.class), any(Expression.class))).thenReturn(exprConcat);
            when(builder.upper(any(Expression.class))).thenReturn(exprUpper);
            when(builder.like(eq(exprUpper), eq("%JUAN%"))).thenReturn(predicate);

            when(personaQuery.select(root)).thenReturn(personaQuery);
            when(personaQuery.where(any(Predicate[].class))).thenReturn(personaQuery);
            when(entityManager.createQuery(personaQuery)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.setMaxResults(10)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.setFirstResult(0)).thenReturn(personaTypedQuery);
            when(personaTypedQuery.getResultList()).thenReturn(List.of(samplePersona));

            Object result = repository.list(params, "L");

            assertThat(result).isNotNull();
            verify(builder).like(eq(exprUpper), eq("%JUAN%"));
        }
    }

    @Nested
    @DisplayName("list() - Modo 'T' (Total de Registros)")
    class TotalModeTests {

        @Test
        @DisplayName("list con mode 'T' retorna conteo total usando countDistinct")
        void list_ModeT_ShouldReturnCount() {
            PersonaListRequest params = new PersonaListRequest();
            params.setPage(1);
            params.setXpage(10);
            params.setOffset(0);

            when(entityManager.getCriteriaBuilder()).thenReturn(builder);
            when(builder.createQuery(Long.class)).thenReturn(countQuery);
            when(countQuery.from(Persona.class)).thenReturn(root);
            when(builder.conjunction()).thenReturn(predicate);
            when(builder.countDistinct(root)).thenReturn(exprCount);
            when(countQuery.select(exprCount)).thenReturn(countQuery);
            when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
            when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
            when(countTypedQuery.getSingleResult()).thenReturn(50L);

            Object result = repository.list(params, "T");

            assertThat(result).isEqualTo(50L);
            verify(builder).countDistinct(root);
            verify(countTypedQuery).getSingleResult();
        }
    }
}
