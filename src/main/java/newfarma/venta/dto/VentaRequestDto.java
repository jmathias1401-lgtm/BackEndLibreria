package newfarma.venta.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class VentaRequestDto {

    @JsonProperty("idVenta")
    @JsonAlias({"idventa", "idVenta"})
    private Long idVenta;

    @NotBlank(message = "La serie no puede estar vacía")
    private String serie;

    @NotBlank(message = "El correlativo no puede estar vacío")
    private String correlativo;

    @JsonProperty("fechaVenta")
    @JsonAlias({"fechaventa", "fechaVenta"})
    private Date fechaVenta;

    @NotNull(message = "El IGV es obligatorio")
    @PositiveOrZero(message = "El IGV no puede ser negativo")
    private Double igv;

    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    private Double subtotal;

    @NotNull(message = "El costo de venta es obligatorio")
    @PositiveOrZero(message = "El costo de venta no puede ser negativo")
    @JsonProperty("costoVenta")
    @JsonAlias({"costoventa", "costoVenta"})
    private Double costoVenta;

    private Cliente cliente;
    private Empleado empleado;

    @JsonProperty("tipoComprobante")
    @JsonAlias({"tipocomprobante", "tipoComprobante"})
    private TipoComprobante tipoComprobante;
}

