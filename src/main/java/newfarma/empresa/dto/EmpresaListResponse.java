package newfarma.empresa.dto;

import lombok.*;
import newfarma.model.empresa;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class EmpresaListResponse {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<empresa> list;
}