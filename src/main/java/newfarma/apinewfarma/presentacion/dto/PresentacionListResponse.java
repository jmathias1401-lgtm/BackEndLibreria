package newfarma.apinewfarma.presentacion.dto;

import lombok.*;
import newfarma.apinewfarma.model.Presentacion;

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
