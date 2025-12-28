package newfarma.tipopersona;
import jakarta.persistence.EntityNotFoundException;
import newfarma.tipopersona.dto.TipoPersonaListRequest;
import newfarma.tipopersona.dto.TipoPersonaListResponse;
import newfarma.model.TipoPersona;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TipoPersonaService implements TipoPersonaServiceI {
    private final TipoPersonaRepository repository;
    private final TipoPersonaRepositoryJPA repositoryJPA;
    public TipoPersonaService(TipoPersonaRepository repository,TipoPersonaRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public TipoPersonaListResponse list(TipoPersonaListRequest params) {
        TipoPersonaListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<TipoPersona> l = (List<TipoPersona>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = TipoPersonaListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(TipoPersona tipopersona){
        BaseResponse response;
        TipoPersona tipopersona1;
        if(tipopersona.getIdtipopersona()!=null)//actualiza un objeto existente
        {
            tipopersona1=repositoryJPA.findById(tipopersona.getIdtipopersona()).get();
            tipopersona1.setNombre(tipopersona.getNombre());
            repositoryJPA.save(tipopersona1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =repositoryJPA.existsTipoPersonaByNombre(tipopersona.getNombre());
            if (!existName){
                repositoryJPA.save(tipopersona);
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
        TipoPersona tipopersona=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (tipopersona.getIdtipopersona()!=null)
        {
            repositoryJPA.delete(tipopersona);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
