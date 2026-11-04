package newfarma.compra.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos para registrar o actualizar una compra a proveedores")
public class CompraRequestDto {

    @Schema(description = "Identificador de la compra (nulo para creación)", example = "1")
    @JsonProperty("idCompra")
    @JsonAlias({"idcompra", "idCompra"})
    private Long idCompra;

    @Schema(description = "Serie del comprobante de compra", example = "F001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La serie no puede estar vacía")
    private String serie;

    @Schema(description = "Número correlativo del comprobante de compra", example = "000001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correlativo no puede estar vacío")
    private String correlativo;

    @Schema(description = "Fecha de emisión de la compra")
    @JsonProperty("fechaCompra")
    @JsonAlias({"fechacompra", "fechaCompra"})
    private Date fechaCompra;

    @Schema(description = "Costo total de la compra", example = "250.50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El costo de compra es obligatorio")
    @PositiveOrZero(message = "El costo de compra no puede ser negativo")
    @JsonProperty("costoCompra")
    @JsonAlias({"costocompra", "costoCompra"})
    private Double costoCompra;

    @Schema(description = "Proveedor al que se realiza la compra")
    private Proveedor proveedor;

    @Schema(description = "Empleado que registra la compra")
    private Empleado empleado;

    @Schema(description = "Tipo de comprobante")
    @JsonProperty("tipoComprobante")
    @JsonAlias({"tipocomprobante", "tipoComprobante"})
    private TipoComprobante tipoComprobante;
}
