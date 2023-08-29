package newfarma.persona;
import newfarma.model.Persona;
import newfarma.persona.dto.PersonaListRequest;
import newfarma.persona.dto.PersonaListResponse;
import newfarma.utils.BaseResponse;
import newfarma.venta.VentaRepositoryJPA;
import newfarma.venta.VentaServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/persona")
public class PersonaController {
    PersonaServiceI serviceI;
    PersonaRepositoryJPA personaRepositoryJPA;
    public PersonaController(PersonaServiceI serviceI, PersonaRepositoryJPA personaRepositoryJPA){
        this.serviceI=serviceI;
        this.personaRepositoryJPA=personaRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<PersonaListResponse>list(PersonaListRequest params){
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
