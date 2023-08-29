package newfarma.productos.dto;

import lombok.*;
import newfarma.model.Producto;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ProductListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Producto> list;
}
