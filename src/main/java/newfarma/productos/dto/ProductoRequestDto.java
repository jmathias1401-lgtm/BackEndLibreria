package newfarma.productos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequestDto {

    private Long idproducto;
    private String codigoproducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String vencimiento;
    private String estado;
    private String composicion;
    private String ubicacion;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El precio de venta es obligatorio")
    @PositiveOrZero(message = "El precio de venta no puede ser negativo")
    private Double precioventa;

    private Double precioblister;
    private Double preciocaja;
    private String codbarra;
    private String imagen_path;

    // IDs de las relaciones (Llaves foráneas)
    private Long idlaboratorio;
    private Long idpresentacion;
    private Integer idunidadmedida;

    // Métodos para compatibilidad con el JSON que envía Angular { idlaboratorio: X }
    @JsonProperty("laboratorio")
    public void unpackLaboratorio(Map<String, Object> lab) {
        if (lab != null && lab.get("idlaboratorio") != null) {
            this.idlaboratorio = Long.valueOf(lab.get("idlaboratorio").toString());
        }
    }

    @JsonProperty("presentacion")
    public void unpackPresentacion(Map<String, Object> pres) {
        if (pres != null && pres.get("idpresentacion") != null) {
            this.idpresentacion = Long.valueOf(pres.get("idpresentacion").toString());
        }
    }

    @JsonProperty("unidadmedida")
    public void unpackUnidadMedida(Map<String, Object> um) {
        if (um != null && um.get("idunidadmedida") != null) {
            this.idunidadmedida = Integer.valueOf(um.get("idunidadmedida").toString());
        }
    }
}
