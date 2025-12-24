package newfarma.estado;

import jakarta.persistence.EntityNotFoundException;
import newfarma.estado.dto.EstadoListRequest;
import newfarma.estado.dto.EstadoListResponse;
import newfarma.model.Estado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EstadoService implements EstadoServiceI {
    private final EstadoRepository repository;
    private final EstadoRepositoryJPA repositoryJPA;
    public EstadoService(EstadoRepository repository, EstadoRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public EstadoListResponse list(EstadoListRequest params) {
        EstadoListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Estado> l = (List<Estado>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = EstadoListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Estado estado){
        BaseResponse response;
        Estado estado1;
        if(estado.getIdestado()!=null)//actualiza un objeto existente
        {
            estado1=repositoryJPA.findById(estado.getIdestado()).get();
            estado1.setEstado(estado.getEstado());
            repositoryJPA.save(estado1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =repositoryJPA.existsEstadoByEstado(estado.getEstado());
            if (!existName){
                repositoryJPA.save(estado);
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
        Estado estado=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (estado.getIdestado()!=null)
        {
            repositoryJPA.delete(estado);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}