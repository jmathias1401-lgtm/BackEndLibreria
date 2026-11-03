package newfarma.cargo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newfarma.model.Estado;

/**
 * DTO de solicitud para creación y actualización de cargos.
 * Aplica normalización en camelCase para Java y admite claves heredadas vía @JsonAlias.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CargoRequestDto {

    @JsonProperty("idCargo")
    @JsonAlias({"idcargo", "idCargo"})
    private Long idCargo;

    @NotBlank(message = "El nombre del cargo no puede estar vacío")
    @JsonProperty("nombreCargo")
    @JsonAlias({"nombrecargo", "nombreCargo"})
    private String nombreCargo;

    private String descripcion;
    private Estado estado;
}
