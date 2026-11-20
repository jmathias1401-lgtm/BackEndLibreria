package newfarma.kardex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KardexRequestDto {
    private Long idKardex;
    private Long idProducto;
    private String tipoOperacion;
    private String motivo;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String usuario;
}

