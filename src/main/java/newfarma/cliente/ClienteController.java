package newfarma.cliente;

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
public class ClienteController {
    private final ClienteServiceI serviceI;
    private final ClienteRepositoryJPA clienteRepositoryJPA;

    public ClienteController(ClienteServiceI serviceI, ClienteRepositoryJPA clienteRepositoryJPA) {
        this.serviceI = serviceI;
        this.clienteRepositoryJPA = clienteRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<ClienteResponse> list(ClienteListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody ClienteRequestDto clienteDto) {
        return new ResponseEntity<>(serviceI.save(clienteDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
