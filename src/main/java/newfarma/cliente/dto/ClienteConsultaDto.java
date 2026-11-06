package newfarma.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteConsultaDto {
    private Long idCliente;
    private Long idPersona;
    private String tipoDocumento; // DNI o RUC
    private String numeroDocumento;
    private String nombreCompleto;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    private String estado;    // Para RUC: ACTIVO
    private String condicion; // Para RUC: HABIDO
    private String origen;    // "LOCAL", "RENIEC", "SUNAT"
    private boolean exito;
    private String mensaje;
}

