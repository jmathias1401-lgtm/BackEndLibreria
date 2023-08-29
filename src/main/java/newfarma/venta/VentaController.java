package newfarma.venta;
import newfarma.model.Persona;
import newfarma.venta.VentaRepositoryJPA;
import newfarma.venta.VentaServiceI;
import newfarma.utils.BaseResponse;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/persona")
public class VentaController {
    VentaServiceI serviceI;
    VentaRepositoryJPA personaRepositoryJPA;
    public VentaController(VentaServiceI serviceI, VentaRepositoryJPA personaRepositoryJPA){
        this.serviceI=serviceI;
        this.personaRepositoryJPA=personaRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<VentaListResponse>list(VentaListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Persona persona )
    {
        return new ResponseEntity(serviceI.save(persona), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}
