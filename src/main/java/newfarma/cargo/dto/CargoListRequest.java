package newfarma.cargo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class CargoListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombrecargo;
    String search;
}
