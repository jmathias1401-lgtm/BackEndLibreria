package newfarma.productos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import newfarma.productos.dto.ProductListRequest;
import newfarma.repository.BaseRepository;
import newfarma.utils.Util;
import newfarma.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ProductoRepository extends BaseRepository {
    private EntityManager entityManager;

    public Object list(ProductListRequest params, String mode) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = mode.equals("L") ? builder.createQuery(Producto.class) : builder.createQuery(Long.class);
        Root root = query.from(Producto.class);

        Map mapParam = Util.dtoTomap(params);
        Predicate criteriaParams = this.addCriterias(builder.conjunction(), builder, root, mapParam, List.of("nombre", "codbarra"), "eq");
        Predicate criteriaSearch = builder.conjunction();

        if (params.getSearch() != null) {
            Predicate search = builder.like(
                    builder.upper(builder.concat(root.get("nombre"), root.get("composicion"))),
                    "%" + params.getSearch().toUpperCase() + "%");
            criteriaSearch = this.addCriterias(search, builder, root, mapParam, List.of("gameid", "name"), params.getSearch());
        }

        List<Predicate> predicates = List.of(criteriaParams, criteriaSearch);

        return executePaginatedQuery(entityManager, query, root, builder, predicates, mode, params.getOffset(), params.getXpage());
    }
}
