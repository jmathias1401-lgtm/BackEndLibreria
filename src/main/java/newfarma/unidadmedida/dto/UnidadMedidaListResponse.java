package newfarma.unidadmedida.dto;

import lombok.*;

import newfarma.model.UnidadMedida;

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
    private List<UnidadMedida> list;
}
