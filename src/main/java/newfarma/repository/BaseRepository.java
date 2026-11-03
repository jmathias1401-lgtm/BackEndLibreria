package newfarma.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseRepository {

    public Predicate addCriterias(Predicate criteria, CriteriaBuilder builder, Root root, Map params, List<String> fields, String method) {
        for (String field : fields) {
            if (params.containsKey(field) && params.get(field) != null) {
                Predicate w = builder.conjunction();
                switch (method) {
                    case "eq":
                        w = builder.equal(root.get(field), params.get(field));
                        break;
                    case "like":
                        w = builder.like(builder.lower(root.get(field)), "%" + params.get(field).toString().toLowerCase() + "%");
                        break;
                    case "gt":
                        w = builder.greaterThan(root.get(field), new Date(params.get(field).toString()));
                        break;
                    case "lt":
                        w = builder.lessThan(root.get(field), new Date(params.get(field).toString()));
                        break;
                    default:
                        break;
                }
                criteria = builder.and(criteria, w);
            }
        }
        return criteria;
    }

    public Object executePaginatedQuery(EntityManager em, CriteriaQuery query, Root root,
                                        CriteriaBuilder builder, List<Predicate> predicates,
                                        String mode, int offset, int maxResults) {
        query.select(mode.equals("L") ? root : builder.countDistinct(root))
                .where(predicates.toArray(new Predicate[0]));

        return mode.equals("L")
                ? em.createQuery(query).setMaxResults(maxResults).setFirstResult(offset).getResultList()
                : (Long) em.createQuery(query).getSingleResult();
    }

    /**
     * Sobrecarga moderna que utiliza Spring Data Pageable.
     */
    public Object executePaginatedQuery(EntityManager em, CriteriaQuery query, Root root,
                                        CriteriaBuilder builder, List<Predicate> predicates,
                                        String mode, Pageable pageable) {
        return executePaginatedQuery(em, query, root, builder, predicates, mode,
                (int) pageable.getOffset(), pageable.getPageSize());
    }

    /**
     * Ejecuta consulta completa y retorna un Page<T> de Spring Data en una sola llamada tipada.
     */
    public <T> Page<T> executePage(EntityManager em, Class<T> entityClass,
                                   List<Predicate> predicates, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // 1. Consulta para datos paginados
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(predicates.toArray(new Predicate[0]));

        List<T> content = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // 2. Consulta para total de elementos
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        countQuery.select(builder.countDistinct(countRoot)).where(predicates.toArray(new Predicate[0]));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
