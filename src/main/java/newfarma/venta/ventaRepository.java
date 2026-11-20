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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaParams);

        if (params.getSearch() != null && !params.getSearch().isBlank()) {
            predicates.add(criteriaSearch);
        }

        if (params.getFechaInicio() != null && !params.getFechaInicio().isBlank()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(params.getFechaInicio().trim());
                predicates.add(builder.greaterThanOrEqualTo(root.get("fechaventa"), start));
            } catch (Exception ignored) {}
        }

        if (params.getFechaFin() != null && !params.getFechaFin().isBlank()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date end = sdf.parse(params.getFechaFin().trim() + " 23:59:59");
                predicates.add(builder.lessThanOrEqualTo(root.get("fechaventa"), end));
            } catch (Exception ignored) {}
        }

        if (params.getIdTipoComprobante() != null && params.getIdTipoComprobante() > 0) {
            predicates.add(builder.equal(root.get("tipoComprobante").get("idtipocomprobante"), params.getIdTipoComprobante()));
        }

        if (params.getIdEmpleado() != null && params.getIdEmpleado() > 0) {
            predicates.add(builder.equal(root.get("empleado").get("idempleado"), params.getIdEmpleado()));
        }

        return executePaginatedQuery(entityManager, query, root, builder, predicates, mode, params.getOffset(), params.getXpage());
    }
}
