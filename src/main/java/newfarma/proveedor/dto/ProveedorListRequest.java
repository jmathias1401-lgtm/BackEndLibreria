package newfarma.proveedor.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class ProveedorListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String ruc;
    String search;
    int id;
}