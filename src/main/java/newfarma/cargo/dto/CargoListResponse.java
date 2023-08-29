package newfarma.cargo.dto;

import lombok.*;
import newfarma.model.Cargo;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class CargoListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Cargo> list;
}
