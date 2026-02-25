package newfarma.empresa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class EmpresaListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String search;
    Long id;
}