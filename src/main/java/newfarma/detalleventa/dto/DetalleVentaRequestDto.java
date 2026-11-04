package newfarma.detalleventa.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newfarma.model.Producto;
import newfarma.model.Venta;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload para crear o actualizar un detalle de venta")
public class DetalleVentaRequestDto {

    @Schema(description = "ID del detalle de venta (null = crear, valor = actualizar)", example = "1")
    @JsonProperty("idDetalleVenta")
    @JsonAlias({"iddetalleventa", "idDetalleVenta"})
    private Long idDetalleVenta;

    @Schema(description = "Código único del detalle de venta", example = "DV-2024-001")
    @JsonProperty("codigoDetalleVenta")
    @JsonAlias({"codigodetalleventa", "codigoDetalleVenta"})
    private String codigoDetalleVenta;

    @Schema(description = "Cantidad de unidades vendidas", example = "5")
    @JsonProperty("unidades")
    @JsonAlias("unidades")
    private Integer unidades;

    @Schema(description = "Costo por unidad", example = "12.50")
    @JsonProperty("costoUnidad")
    @JsonAlias({"costounidad", "costoUnidad"})
    private Double costoUnidad;

    @Schema(description = "Subtotal antes de descuento", example = "62.50")
    @JsonProperty("subtotal")
    @JsonAlias("subtotal")
    private Double subtotal;

    @Schema(description = "Descuento por unidad", example = "1.00")
    @JsonProperty("descuentoUnidad")
    @JsonAlias({"descuentounidad", "descuentoUnidad"})
    private Double descuentoUnidad;

    @Schema(description = "Total final del detalle", example = "57.50")
    @JsonProperty("total")
    @JsonAlias("total")
    private Double total;

    @Schema(description = "Venta asociada (referencia por ID)")
    @JsonProperty("venta")
    @JsonAlias("venta")
    private Venta venta;

    @Schema(description = "Producto asociado (referencia por ID)")
    @JsonProperty("producto")
    @JsonAlias("producto")
    private Producto producto;
}
