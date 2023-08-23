package newfarma.apinewfarma.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseRepository {
    public Predicate addCriterias(Predicate criteria, CriteriaBuilder builder , Root root, Map params, List<String> fields, String method){

        for (String field:fields) {
            if( params.containsKey(field) && params.get(field)!=null ){
                Predicate w=builder.conjunction();
                switch (method){
                    case "eq": w=builder.equal(root.get(field), params.get(field) ); break;
                    case "like":w=builder.like(builder.lower(root.get(field)), "%" + params.get(field).toString().toLowerCase() + "%"); break;
                    //case "greatThan": w= builder.greaterThan(root.get(field), params.get(field)); break;
                    case "gt": w= builder.greaterThan( root.get(field), new Date(params.get(field).toString()) ) ; break;
                    case "lt": w= builder.lessThan( root.get(field), new Date(params.get(field).toString()) ) ; break;
                }
              
                criteria=builder.and(criteria,w);
            }
        }
        return criteria;
    }
}
