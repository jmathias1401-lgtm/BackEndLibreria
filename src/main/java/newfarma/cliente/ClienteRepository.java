package newfarma.cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.model.Cliente;
import newfarma.repository.BaseRepository;
import newfarma.utils.Util;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ClienteRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(ClienteListRequest params, String mode) {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(Cliente.class) : builder.createQuery(Long.class);
        Root root = query.from(Cliente.class);
        query.orderBy(builder.desc(root.get("id")));
        Predicate criteriaParams = builder.conjunction();
        Predicate criteriaSearch = builder.conjunction();
        List<String> eqFields = new ArrayList<String>() {{add("idcliente");}};
        List<String> likeFields = new ArrayList<String>() {{add("idcliente");}};
        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");
        // Manual filter for persona_idpersona
        if (params.getPersona_idpersona() != null) {
            criteriaParams = builder.and(criteriaParams, builder.equal(root.get("persona").get("idpersona"), params.getPersona_idpersona()));
        }
        // WITH THAT IS THE LIKE
        if (params.getSearch() != null) {
            Predicate search = builder.like(root.get("idcliente").as(String.class),"%" + params.getSearch().toUpperCase() + "%");
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
