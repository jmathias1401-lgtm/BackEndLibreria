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

@RestController
@RequestMapping("api/persona")
public class PersonaController {
    private final PersonaServiceI serviceI;
    private final PersonaRepositoryJPA personaRepositoryJPA;

    public PersonaController(PersonaServiceI serviceI, PersonaRepositoryJPA personaRepositoryJPA) {
        this.serviceI = serviceI;
        this.personaRepositoryJPA = personaRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<PersonaListResponse> list(PersonaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Persona persona) {
        return new ResponseEntity<>(serviceI.save(persona), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
