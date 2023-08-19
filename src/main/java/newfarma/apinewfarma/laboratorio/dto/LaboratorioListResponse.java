package newfarma.apinewfarma.laboratorio.dto;

import lombok.*;
import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.model.Producto;

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
