package newfarma.apinewfarma.unidadmedida;
import jakarta.persistence.EntityNotFoundException;
import newfarma.apinewfarma.model.UnidadMedida;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadMedidaService implements UnidadMedidaServiceI {

    private final UnidadMedidaRepository repository;
    private final UnidadMedidaRepositoryJPA unidadMedidaRepositoryJPA;
    public UnidadMedidaService(UnidadMedidaRepository repository,UnidadMedidaRepositoryJPA unidadMedidaRepositoryJPA){
        this.repository=repository;
        this.unidadMedidaRepositoryJPA=unidadMedidaRepositoryJPA;
    }
    @Override
    public UnidadMedidaListResponse list(UnidadMedidaListRequest params) {
        UnidadMedidaListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Laboratorio> l = (List<Laboratorio>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = UnidadMedidaListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(UnidadMedida unidadMedida){
        BaseResponse response;
        UnidadMedida unidadMedida1;
        if(unidadMedida.getIdunidadmedida()!=null)//actualiza un objeto existente
        {
            unidadMedida1=unidadMedidaRepositoryJPA.findById(unidadMedida.getIdunidadmedida()).get();
            unidadMedida1.setNombreunidad(unidadMedida.getNombreunidad());
            unidadMedidaRepositoryJPA.save(unidadMedida1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =unidadMedidaRepositoryJPA.existsUnidadMedidaByNombreunidad(unidadMedida.getNombreunidad());
            if (!existName){
                unidadMedidaRepositoryJPA.save(unidadMedida);
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
        UnidadMedida unidadMedida=unidadMedidaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (unidadMedida.getIdunidadmedida()!=null)
        {
            unidadMedidaRepositoryJPA.delete(unidadMedida);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
