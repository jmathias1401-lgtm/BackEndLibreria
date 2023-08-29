package newfarma.presentacion.dto;

import lombok.*;
import newfarma.model.Presentacion;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class PresentacionListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Presentacion> list;
}
