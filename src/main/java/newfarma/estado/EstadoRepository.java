package newfarma.estado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import newfarma.estado.dto.EstadoListRequest;
import newfarma.model.Estado;
import newfarma.repository.BaseRepository;
import newfarma.utils.Util;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class EstadoRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(EstadoListRequest params, String mode) {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(Estado.class) : builder.createQuery(Long.class);
        Root root = query.from(Estado.class);
        Predicate criteriaParams = builder.conjunction();
        Predicate criteriaSearch = builder.conjunction();
        List<String> eqFields = new ArrayList<String>() {{add("idestado");add("estado");}};
        List<String> likeFields = new ArrayList<String>() {{add("idestado");add("estado");}};
        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");
        // WITH THAT IS THE LIKE
        if (params.getSearch() != null) {
            Predicate search = builder.like(root.get("estado"),"%" + params.getSearch().toUpperCase() + "%");
            criteriaSearch = this.addCriterias(search, builder, root, mapParam, likeFields, params.getSearch());
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