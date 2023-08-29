package newfarma.persona;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Persona;
import newfarma.model.Producto;
import newfarma.persona.dto.PersonaListRequest;
import newfarma.persona.dto.PersonaListResponse;
import newfarma.utils.BaseResponse;
import newfarma.venta.VentaRepositoryJPA;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService implements PersonaServiceI {
    private final PersonaRepository repository;
    VentaRepositoryJPA PersonaRepositoryJPA;
    public PersonaService(VentaRepositoryJPA PersonaRepositoryJPA, PersonaRepository repository){
        this.PersonaRepositoryJPA=PersonaRepositoryJPA;
        this.repository=repository;
    }
    @Override
    public PersonaListResponse list(PersonaListRequest params) {
        PersonaListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Producto> l = (List<Producto>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = PersonaListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Persona persona){
        BaseResponse response;
        Persona persona1;
        if(persona.getIdpersona()!=null)//actualiza un objeto existente
        {
            persona1=PersonaRepositoryJPA.findById(persona.getIdpersona()).get();
            persona1.setDni(persona.getDni());
            persona1.setRuc(persona.getRuc());
            persona1.setNombre(persona.getNombre());
            persona1.setMaterno(persona.getMaterno());
            persona1.setPaterno(persona.getPaterno());
            persona1.setFechanacimiento(persona.getFechanacimiento());
            persona1.setTelefono(persona.getTelefono());
            persona1.setCorreo(persona.getCorreo());
            persona1.setSexo(persona.getSexo());
            persona1.setDireccion(persona.getDireccion());
            persona1.setTipoPersona(persona.getTipoPersona());
            PersonaRepositoryJPA.save(persona1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =PersonaRepositoryJPA.existsPersonaByDni(persona.getDni());
            if (!existName){
                PersonaRepositoryJPA.save(persona);
                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }else{
                response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
            }
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Persona persona=PersonaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (persona.getIdpersona()!=null)
        {
            PersonaRepositoryJPA.delete(persona);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
