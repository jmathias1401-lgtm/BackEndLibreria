package newfarma.apinewfarma.presentacion;
import jakarta.persistence.EntityNotFoundException;
import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.presentacion.dto.PresentacionListRequest;
import newfarma.apinewfarma.model.Presentacion;
import newfarma.apinewfarma.presentacion.dto.PresentacionListResponse;
import newfarma.apinewfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresentacionService implements PresentacionServiceI {

    private final PresentacionRepository repository;
    private final PresentacionRepositoryJPA presentacionRepositoryJPA;
    public PresentacionService(PresentacionRepository repository,PresentacionRepositoryJPA presentacionRepositoryJPA){
        this.repository=repository;
        this.presentacionRepositoryJPA=presentacionRepositoryJPA;
    }

    @Override
    public PresentacionListResponse list(PresentacionListRequest params) {
        PresentacionListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Presentacion> l = (List<Presentacion>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = PresentacionListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Presentacion presentacion){
        BaseResponse response;
        Presentacion presentacion1;
        if(presentacion.getIdpresentacion()!=null)//actualiza un objeto existente
        {
            presentacion1=presentacionRepositoryJPA.findById(presentacion.getIdpresentacion()).get();
            presentacion1.setNombrepresentacion(presentacion.getNombrepresentacion());
            presentacionRepositoryJPA.save(presentacion1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =presentacionRepositoryJPA.existsLaboratorioByNombrepresentacion(presentacion.getNombrepresentacion());
            if (!existName){
                presentacionRepositoryJPA.save(presentacion);
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
        Presentacion presentacion=presentacionRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (presentacion.getIdpresentacion()!=null)
        {
            presentacionRepositoryJPA.delete(presentacion);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
