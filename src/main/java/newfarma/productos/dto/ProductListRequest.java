package newfarma.productos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class ProductListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String codbarra;
    String search;
    int id;
}
