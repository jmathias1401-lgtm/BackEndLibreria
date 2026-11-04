package newfarma.cliente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteRequestDto;
import newfarma.cliente.dto.ClienteResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cliente")
@Tag(name = "Clientes", description = "Gestión de clientes vinculados a personas naturales o jurídicas")
public class ClienteController {
    private final ClienteServiceI serviceI;
    private final ClienteRepositoryJPA clienteRepositoryJPA;

    public ClienteController(ClienteServiceI serviceI, ClienteRepositoryJPA clienteRepositoryJPA) {
        this.serviceI = serviceI;
        this.clienteRepositoryJPA = clienteRepositoryJPA;
    }

    @Operation(summary = "Listar clientes", description = "Obtiene una lista paginada y filtrada de clientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<ClienteResponse> list(ClienteListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar cliente", description = "Registra un nuevo cliente o actualiza uno existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente registrado/actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de cliente inválidos"),
            @ApiResponse(responseCode = "404", description = "Persona asociada no encontrada")
    })
    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody ClienteRequestDto clienteDto) {
        return new ResponseEntity<>(serviceI.save(clienteDto), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del cliente", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
