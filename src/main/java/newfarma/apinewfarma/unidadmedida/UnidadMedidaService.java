package newfarma.apinewfarma.unidadmedida;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.apinewfarma.model.Laboratorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadMedidaService implements UnidadMedidaServiceI {

    private final UnidadMedidaRepository repository;

    public UnidadMedidaService(UnidadMedidaRepository repository){
        this.repository=repository;
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
}
