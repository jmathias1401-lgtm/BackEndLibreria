package newfarma.tipopersona;
import newfarma.tipopersona.dto.TipoPersonaListRequest;
import newfarma.tipopersona.dto.TipoPersonaListResponse;
import newfarma.model.TipoPersona;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/tipopersona")
class TipoPersonaController {
    TipoPersonaServiceI serviceI;

    public TipoPersonaController(TipoPersonaServiceI serviceI){
        this.serviceI=serviceI;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<TipoPersonaListResponse>list(TipoPersonaListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody TipoPersona tipopersona )
    {
        return new ResponseEntity(serviceI.save(tipopersona), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }
}
