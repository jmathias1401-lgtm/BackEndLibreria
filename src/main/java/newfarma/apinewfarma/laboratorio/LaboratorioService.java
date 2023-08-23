package newfarma.apinewfarma.laboratorio;
import jakarta.persistence.EntityNotFoundException;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class LaboratorioService implements LaboratorioServiceI {
    private final LaboratorioRepository repository;
    private final LaboratorioRepositoryJPA repositoryJPA;
    public LaboratorioService(LaboratorioRepository repository,LaboratorioRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public LaboratorioListResponse list(LaboratorioListRequest params) {
        LaboratorioListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Laboratorio> l = (List<Laboratorio>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = LaboratorioListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Laboratorio laboratorio){
        BaseResponse response;
        Laboratorio laboratorio1;
        if(laboratorio.getIdlaboratorio()!=null)//actualiza un objeto existente
        {
            laboratorio1=repositoryJPA.findById(laboratorio.getIdlaboratorio()).get();
            laboratorio1.setNombrelaboratorio(laboratorio.getNombrelaboratorio());
            repositoryJPA.save(laboratorio1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =repositoryJPA.existsLaboratorioByNombrelaboratorio(laboratorio.getNombrelaboratorio());
            if (!existName){
                repositoryJPA.save(laboratorio);
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
        Laboratorio laboratorio=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (laboratorio.getIdlaboratorio()!=null)
        {
            repositoryJPA.delete(laboratorio);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
