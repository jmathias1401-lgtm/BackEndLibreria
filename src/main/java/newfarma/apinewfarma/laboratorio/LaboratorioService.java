package newfarma.apinewfarma.laboratorio;

import newfarma.apinewfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
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
        // Laboratorio laboratorio1 =laboratorioRepositoryJPA.findById(1L).orElseThrow(EntityNotFoundException::new);
        BaseResponse response;
        boolean existName =repositoryJPA.existsLaboratorioByNombrelaboratorio(laboratorio.getNombrelaboratorio());
        if (!existName){
            repositoryJPA.save(laboratorio);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SUCESS").build();
        }else{
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }

        return response;
    }

}
