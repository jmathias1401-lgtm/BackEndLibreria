package newfarma.compra.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newfarma.model.Empleado;
import newfarma.model.Proveedor;
import newfarma.model.TipoComprobante;

import java.util.Date;

/**
 * DTO de solicitud para creación y actualización de compras.
 * Aplica normalización en camelCase para Java y admite claves heredadas vía @JsonAlias.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompraRequestDto {

    @JsonProperty("idCompra")
    @JsonAlias({"idcompra", "idCompra"})
    private Long idCompra;

    @NotBlank(message = "La serie no puede estar vacía")
    private String serie;

    @NotBlank(message = "El correlativo no puede estar vacío")
    private String correlativo;

    @JsonProperty("fechaCompra")
    @JsonAlias({"fechacompra", "fechaCompra"})
    private Date fechaCompra;

    @NotNull(message = "El costo de compra es obligatorio")
    @PositiveOrZero(message = "El costo de compra no puede ser negativo")
    @JsonProperty("costoCompra")
    @JsonAlias({"costocompra", "costoCompra"})
    private Double costoCompra;

    private Proveedor proveedor;
    private Empleado empleado;

    @JsonProperty("tipoComprobante")
    @JsonAlias({"tipocomprobante", "tipoComprobante"})
    private TipoComprobante tipoComprobante;
}

