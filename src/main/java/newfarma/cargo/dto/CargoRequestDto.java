package newfarma.cargo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos para registrar o actualizar un cargo de trabajo")
public class CargoRequestDto {

    @Schema(description = "Identificador del cargo (nulo para creación)", example = "1")
    @JsonProperty("idCargo")
    @JsonAlias({"idcargo", "idCargo"})
    private Long idCargo;

    @Schema(description = "Nombre o denominación del cargo", example = "FARMACÉUTICO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del cargo no puede estar vacío")
    @JsonProperty("nombreCargo")
    @JsonAlias({"nombrecargo", "nombreCargo"})
    private String nombreCargo;

    @Schema(description = "Descripción de funciones o responsabilidades del cargo", example = "Responsable de la dispensación de medicamentos")
    private String descripcion;

    @Schema(description = "Estado actual del cargo (Activo, Inactivo)")
    private Estado estado;
}
