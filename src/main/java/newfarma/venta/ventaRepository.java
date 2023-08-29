package newfarma.venta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import newfarma.model.Persona;
import newfarma.venta.dto.VentaListRequest;
import newfarma.repository.BaseRepository;
import newfarma.utils.Util;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ventaRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(VentaListRequest params, String mode) {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(Persona.class) : builder.createQuery(Long.class);
        Root root = query.from(Persona.class);
        Predicate criteriaParams = builder.conjunction();
        Predicate criteriaSearch = builder.conjunction();
        List<String> eqFields = new ArrayList<String>() {{add("nombre");add("apellido");}};
        List<String> likeFields = new ArrayList<String>() {{add("gameid");add("name");}};
        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");
        // WITH THAT IS THE LIKE
        if (params.getSearch() != null) {
            Predicate search = builder.like(
                    builder.upper(builder.concat(builder.concat(root.get("name"), root.get("provider")),
                            builder.concat(builder.concat(root.get("brand"), root.get("gameid")),
                                    builder.concat(root.get("category"), root.get("type"))))),
                    "%" + params.getSearch().toUpperCase() + "%");
            criteriaSearch = this.addCriterias(search, builder, root, mapParam, likeFields, params.getSearch());
        }
        predicates.add(criteriaParams);
        predicates.add(criteriaSearch);
        query.select(mode.equals("L") ? root : builder.countDistinct(root)).where(predicates.toArray(new Predicate[0]));
        response = mode.equals("L")
                ? entityManager.createQuery(query).setMaxResults(params.getXpage()).setFirstResult(params.getOffset())
                .getResultList()
                : (Long) entityManager.createQuery(query).getSingleResult();
        return response;
    }
}
