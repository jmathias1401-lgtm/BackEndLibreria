package newfarma.cargo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class CargoListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    Integer idcargo;
    String nombrecargo;
    String search;
}
