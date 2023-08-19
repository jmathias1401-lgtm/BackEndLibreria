package newfarma.apinewfarma.unidadmedida.dto;

import lombok.*;
import newfarma.apinewfarma.model.Laboratorio;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class UnidadMedidaListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Laboratorio> list;
}
