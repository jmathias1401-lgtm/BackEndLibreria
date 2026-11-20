package newfarma.kardex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newfarma.model.Kardex;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KardexListResponse {
    private Integer total;
    private List<Kardex> list;
}

