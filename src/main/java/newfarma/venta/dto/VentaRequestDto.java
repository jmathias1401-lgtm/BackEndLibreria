package newfarma.venta.dto;

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
import newfarma.model.Cliente;
import newfarma.model.Empleado;
import newfarma.model.TipoComprobante;

import java.util.Date;

/**
 * DTO de solicitud para creación y actualización de ventas.
 * Aplica normalización en camelCase para Java y admite claves heredadas vía @JsonAlias.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para registrar o actualizar una venta comercial")
public class VentaRequestDto {

    @Schema(description = "Identificador de la venta (nulo para creación)", example = "1")
    @JsonProperty("idVenta")
    @JsonAlias({"idventa", "idVenta"})
    private Long idVenta;

    @Schema(description = "Serie del comprobante de pago", example = "B001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La serie no puede estar vacía")
    private String serie;

    @Schema(description = "Número correlativo del comprobante", example = "000001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correlativo no puede estar vacío")
    private String correlativo;

    @Schema(description = "Fecha de emisión de la venta")
    @JsonProperty("fechaVenta")
    @JsonAlias({"fechaventa", "fechaVenta"})
    private Date fechaVenta;

    @Schema(description = "Monto de IGV", example = "18.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El IGV es obligatorio")
    @PositiveOrZero(message = "El IGV no puede ser negativo")
    private Double igv;

    @Schema(description = "Subtotal de la venta", example = "100.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    private Double subtotal;

    @Schema(description = "Costo total de la venta", example = "118.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El costo de venta es obligatorio")
    @PositiveOrZero(message = "El costo de venta no puede ser negativo")
    @JsonProperty("costoVenta")
    @JsonAlias({"costoventa", "costoVenta"})
    private Double costoVenta;

    @Schema(description = "Cliente asociado a la venta")
    private Cliente cliente;

    @Schema(description = "Empleado que realiza la venta")
    private Empleado empleado;

    @Schema(description = "Tipo de comprobante emitido (Boleta, Factura, etc.)")
    @JsonProperty("tipoComprobante")
    @JsonAlias({"tipocomprobante", "tipoComprobante"})
    private TipoComprobante tipoComprobante;
}
