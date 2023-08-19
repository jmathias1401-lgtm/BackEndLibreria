package newfarma.apinewfarma.laboratorio;

import newfarma.apinewfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.apinewfarma.model.Laboratorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratorioService implements LaboratorioServiceI {

    private final LaboratorioRepository repository;

    public LaboratorioService(LaboratorioRepository repository){
        this.repository=repository;
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
}
