package newfarma.unidadmedida;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.model.UnidadMedida;
import newfarma.repository.BaseRepository;
import newfarma.utils.Util;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class UnidadMedidaRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(UnidadMedidaListRequest params, String mode) {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(UnidadMedida.class) : builder.createQuery(Long.class);
        Root root = query.from(UnidadMedida.class);
        Predicate criteriaParams = builder.conjunction();

        List<String> eqFields = new ArrayList<String>() {{add("idunidadmedida");add("nombreunidad");}};
        List<String> likeFields = new ArrayList<String>() {{add("idunidadmedida");add("nombreunidad");}};
        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");

        predicates.add(criteriaParams);

        query.select(mode.equals("L") ? root : builder.countDistinct(root)).where(predicates.toArray(new Predicate[0]));
        response = mode.equals("L")
                ? entityManager.createQuery(query).setMaxResults(params.getXpage()).setFirstResult(params.getOffset())
                .getResultList()
                : (Long) entityManager.createQuery(query).getSingleResult();
        return response;
    }
}
