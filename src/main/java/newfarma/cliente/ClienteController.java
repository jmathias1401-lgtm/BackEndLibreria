package newfarma.cliente;

import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteResponse;
import newfarma.model.Cliente;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/cliente")
public class ClienteController {
    ClienteServiceI serviceI;

    public ClienteController(ClienteServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<ClienteResponse> list(ClienteListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Cliente cliente )
    {
        return new ResponseEntity(serviceI.save(cliente), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}
