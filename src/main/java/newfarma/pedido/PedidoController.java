package newfarma.pedido;

import newfarma.pedido.dto.PedidoListRequest;
import newfarma.pedido.dto.PedidoListResponse;
import newfarma.model.Pedido;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/pedido")
public class PedidoController {
    PedidoServiceI serviceI;

    public PedidoController(PedidoServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping

    public ResponseEntity<PedidoListResponse> list(PedidoListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @PostMapping

    public ResponseEntity<BaseResponse> save(@RequestBody Pedido pedido )
    {
        return new ResponseEntity(serviceI.save(pedido), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
    @GetMapping("/lastPedido")
    public ResponseEntity<Map<String, String>> lastPedido() throws Exception
    {
        String dato= serviceI.lastPedido();
        Map<String, String> response = Map.of("codigopedido", dato);
        ResponseEntity ok = new ResponseEntity(response,HttpStatus.OK);
        return ok;
    }
}