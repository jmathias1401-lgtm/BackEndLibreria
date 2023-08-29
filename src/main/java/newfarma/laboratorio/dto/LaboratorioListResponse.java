package newfarma.laboratorio.dto;

import lombok.*;
import newfarma.model.Laboratorio;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class LaboratorioListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<Laboratorio> list;
}
