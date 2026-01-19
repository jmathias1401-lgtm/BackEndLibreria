package newfarma.venta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import newfarma.model.Cliente;
import newfarma.model.Persona;
import newfarma.model.Venta;
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

    public Object list(VentaListRequest params, String mode)
    {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(Venta.class) : builder.createQuery(Long.class);
        Root root = query.from(Venta.class);

        Join<Venta, Cliente> clienteJoin = null;
        Join<Cliente, Persona> personaJoin = null;

        if (params.getSearch() != null) {
            clienteJoin = root.join("cliente", JoinType.INNER);
            personaJoin = clienteJoin.join("persona");
        }

        query.orderBy(builder.desc(root.get("id")));

        Predicate criteriaParams = builder.conjunction();
        Predicate criteriaSearch = builder.conjunction();
        List<String> eqFields = new ArrayList<String>() {{add("serie");add("correlativo");}};
        List<String> likeFields = new ArrayList<String>() {{add("serie");add("correlativo");}};

        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");

        // WITH THAT IS THE LIKE
        if (params.getSearch() != null) {
            Predicate nombrePredicate = builder.like(
                    personaJoin.get("nombre"),
                    "%" + params.getSearch() + "%"
            );
            criteriaSearch = this.addCriterias(criteriaSearch, builder, root, mapParam, likeFields, params.getSearch());
            criteriaSearch = builder.and(criteriaSearch, nombrePredicate);
        }

        predicates.add(criteriaParams);
        predicates.add(criteriaSearch);

        query.select(mode.equals("L") ? root : builder.countDistinct(root))
                .where(predicates.toArray(new Predicate[0]));

        response = mode.equals("L")? entityManager.createQuery(query).setMaxResults(params.getXpage()).setFirstResult(params.getOffset())
                .getResultList()
                : (Long) entityManager.createQuery(query).getSingleResult();

        return response;
    }
}
