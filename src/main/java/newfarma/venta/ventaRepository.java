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

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ventaRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(VentaListRequest params, String mode) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = mode.equals("L") ? builder.createQuery(Venta.class) : builder.createQuery(Long.class);
        Root root = query.from(Venta.class);

        query.orderBy(builder.desc(root.get("id")));

        Map mapParam = Util.dtoTomap(params);
        Predicate criteriaParams = this.addCriterias(builder.conjunction(), builder, root, mapParam, List.of("serie", "correlativo"), "eq");
        Predicate criteriaSearch = builder.conjunction();

        if (params.getSearch() != null) {
            Join<Venta, Cliente> clienteJoin = root.join("cliente", JoinType.INNER);
            Join<Cliente, Persona> personaJoin = clienteJoin.join("persona");

            Predicate nombrePredicate = builder.like(
                    personaJoin.get("nombre"),
                    "%" + params.getSearch() + "%"
            );
            criteriaSearch = this.addCriterias(criteriaSearch, builder, root, mapParam, List.of("serie", "correlativo"), params.getSearch());
            criteriaSearch = builder.and(criteriaSearch, nombrePredicate);
        }

        List<Predicate> predicates = List.of(criteriaParams, criteriaSearch);

        return executePaginatedQuery(entityManager, query, root, builder, predicates, mode, params.getOffset(), params.getXpage());
    }
}
