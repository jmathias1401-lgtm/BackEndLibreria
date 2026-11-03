package newfarma.cliente.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ClienteRequestDto {

    @JsonProperty("idCliente")
    @JsonAlias({"idcliente", "idCliente"})
    private Long idCliente;

    @NotNull(message = "Los datos de la persona asociada son obligatorios")
    private Persona persona;
}

