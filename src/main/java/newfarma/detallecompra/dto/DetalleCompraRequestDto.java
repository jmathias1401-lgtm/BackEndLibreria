package newfarma.detallecompra.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newfarma.model.Compra;
import newfarma.model.Producto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload para crear o actualizar un detalle de compra")
public class DetalleCompraRequestDto {

    @Schema(description = "ID del detalle de compra (null = crear, valor = actualizar)", example = "1")
    @JsonProperty("idDetalleCompra")
    @JsonAlias({"iddetallecompra", "idDetalleCompra"})
    private Long idDetalleCompra;

    @Schema(description = "Código único del detalle de compra", example = "DC-2024-001")
    @JsonProperty("codigoDetalleCompra")
    @JsonAlias({"codigodetallecompra", "codigoDetalleCompra"})
    private String codigoDetalleCompra;

    @Schema(description = "Lote del producto en la compra", example = "LOTE-A1")
    @JsonProperty("lote")
    @JsonAlias("lote")
    private String lote;

    @Schema(description = "Cantidad de unidades compradas", example = "10")
    @JsonProperty("unidades")
    @JsonAlias("unidades")
    private Integer unidades;

    @Schema(description = "Costo por unidad", example = "8.00")
    @JsonProperty("costoUnidad")
    @JsonAlias({"costounidad", "costoUnidad"})
    private Double costoUnidad;

    @Schema(description = "Total del detalle de compra", example = "80.00")
    @JsonProperty("total")
    @JsonAlias("total")
    private Double total;

    @Schema(description = "Compra asociada (referencia por ID)")
    @JsonProperty("compra")
    @JsonAlias("compra")
    private Compra compra;

    @Schema(description = "Producto asociado (referencia por ID)")
    @JsonProperty("producto")
    @JsonAlias("producto")
    private Producto producto;
}
