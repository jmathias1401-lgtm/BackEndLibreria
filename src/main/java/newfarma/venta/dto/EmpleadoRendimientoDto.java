package newfarma.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpleadoRendimientoDto {
    private Long idEmpleado;
    private String empleado;
    private Double ventasHoy;
    private Double ventasSemana;
    private Double ventasMes;
    private Double ventasAnio;
    private Integer totalTransacciones;
}
