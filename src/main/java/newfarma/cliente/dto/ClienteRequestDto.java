package newfarma.cliente.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newfarma.model.Persona;

/**
 * DTO de solicitud para creación y actualización de clientes.
 * Aplica normalización en camelCase para Java y admite claves heredadas vía @JsonAlias.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para registrar o actualizar un cliente")
public class ClienteRequestDto {

    @Schema(description = "Identificador del cliente (nulo para creación)", example = "1")
    @JsonProperty("idCliente")
    @JsonAlias({"idcliente", "idCliente"})
    private Long idCliente;

    @Schema(description = "Datos de la persona vinculada al cliente", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Los datos de la persona asociada son obligatorios")
    private Persona persona;
}
