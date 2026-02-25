package newfarma.laboratorio.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class LaboratorioListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombrelaboratorio;
    String search;
    Integer idlaboratorio;
}
