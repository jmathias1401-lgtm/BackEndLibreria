package newfarma.persona;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.Persona;
import newfarma.persona.dto.PersonaListRequest;
import newfarma.persona.dto.PersonaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/persona")
@Tag(name = "Personas", description = "Gestión de datos personales, identificaciones (DNI) y direcciones")
public class PersonaController {
    private final PersonaServiceI serviceI;
    private final PersonaRepositoryJPA personaRepositoryJPA;

    public PersonaController(PersonaServiceI serviceI, PersonaRepositoryJPA personaRepositoryJPA) {
        this.serviceI = serviceI;
        this.personaRepositoryJPA = personaRepositoryJPA;
    }

    @Operation(summary = "Listar personas", description = "Obtiene una lista paginada y filtrada de personas")
    @GetMapping
    public ResponseEntity<PersonaListResponse> list(PersonaListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar persona", description = "Crea o actualiza los datos de una persona")
    @PostMapping
    public ResponseEntity<BaseResponse> save(@RequestBody Persona persona) {
        return new ResponseEntity<>(serviceI.save(persona), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar persona", description = "Elimina el registro de una persona por su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único de la persona", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
