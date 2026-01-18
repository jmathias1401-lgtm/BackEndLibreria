package newfarma.venta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    public Object list(VentaListRequest params, String mode) {
        Object response;
        CriteriaQuery query;
        List<Predicate> predicates = new ArrayList<>();
        Map mapParam = Util.dtoTomap(params);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        query = mode.equals("L") ? builder.createQuery(Venta.class) : builder.createQuery(Long.class);
        Root root = query.from(Venta.class);
        Root client=query.from(Cliente.class);
        Root persona =query.from(Persona.class);
        query.orderBy(builder.desc(root.get("id")));
        //Predicate joinClientPredicate = builder.conjunction();
        //Predicate joinPersonaPredicate = builder.conjunction();
        Predicate criteriaParams = builder.conjunction();
        Predicate criteriaSearch = builder.conjunction();
        List<String> eqFields = new ArrayList<String>() {{add("nombre");add("apellido");}};
        List<String> likeFields = new ArrayList<String>() {{add("nombre");add("apellido");}};
        criteriaParams = this.addCriterias(criteriaParams, builder, root, mapParam, eqFields, "eq");
        // WITH THAT IS THE LIKE
        if (params.getSearch() != null) {
           // joinClientPredicate = builder.equal(root.get("cliente"), client.get("idcliente"));
            //joinPersonaPredicate = builder.equal(client.get("persona"),persona.get("idpersona"));
            Predicate search = builder.like(
                    builder.upper(builder.concat(
                            builder.concat(persona.get("nombre"), persona.get("paterno")),
                            builder.concat(persona.get("dni"), persona.get("materno")
                                          ))),
                    "%" + params.getSearch().toUpperCase() + "%");
            criteriaSearch = this.addCriterias(search, builder, root, mapParam, likeFields, params.getSearch());
        }
        predicates.add(criteriaParams);
        predicates.add(criteriaSearch);
        //predicates.add(joinClientPredicate);
        //predicates.add(joinPersonaPredicate);
        query.select(mode.equals("L") ? root : builder.countDistinct(root)).where(predicates.toArray(new Predicate[0]));
        response = mode.equals("L")
                ? entityManager.createQuery(query).setMaxResults(params.getXpage()).setFirstResult(params.getOffset())
                .getResultList()
                : (Long) entityManager.createQuery(query).getSingleResult();
        return response;
    }
}
