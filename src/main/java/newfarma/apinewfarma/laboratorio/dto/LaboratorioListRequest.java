package newfarma.apinewfarma.laboratorio.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class LaboratorioListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    String codbarra;
    String search;
}
