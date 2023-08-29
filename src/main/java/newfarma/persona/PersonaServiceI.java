package newfarma.persona;

import newfarma.model.Persona;
import newfarma.persona.dto.PersonaListRequest;
import newfarma.persona.dto.PersonaListResponse;
import newfarma.utils.BaseResponse;

public interface PersonaServiceI {
    PersonaListResponse list(PersonaListRequest params);

    BaseResponse save(Persona persona);

    BaseResponse eliminar(Long id);
}
