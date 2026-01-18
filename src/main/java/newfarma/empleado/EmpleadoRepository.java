package newfarma.empleado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.model.Empleado;
import newfarma.repository.BaseRepository;
import newfarma.utils.Util;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class EmpleadoRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(EmpleadoListRequest params, String mode) {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(Empleado.class) : builder.createQuery(Long.class);
        Root root = query.from(Empleado.class);
        Predicate criteriaParams = builder.conjunction();
        Predicate criteriaSearch = builder.conjunction();
        List<String> eqFields = new ArrayList<String>() {{add("idempleado");add("estado");add("persona");add("usuario");}};
        List<String> likeFields = new ArrayList<String>() {{add("idempleado");}};
        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");
        // WITH THAT IS THE LIKE
        if (params.getSearch() != null) {
            Predicate search = builder.like(builder.upper(root.get("persona").get("nombre")), "%" + params.getSearch().toUpperCase() + "%");
            criteriaSearch = search;
        }
        predicates.add(criteriaParams);
        predicates.add(criteriaSearch);
        query.select(mode.equals("L") ? root : builder.count(root)).where(predicates.toArray(new Predicate[0]));
        response = mode.equals("L")
                ? entityManager.createQuery(query).setMaxResults(params.getXpage()).setFirstResult(params.getOffset())
                .getResultList()
                : (Long) entityManager.createQuery(query).getSingleResult();
        return response;
    }
}