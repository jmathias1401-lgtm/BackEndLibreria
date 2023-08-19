package newfarma.apinewfarma.presentacion;
import newfarma.apinewfarma.presentacion.dto.PresentacionListRequest;
import newfarma.apinewfarma.model.Presentacion;
import newfarma.apinewfarma.presentacion.dto.PresentacionListResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresentacionService implements PresentacionServiceI {

    private final PresentacionRepository repository;

    public PresentacionService(PresentacionRepository repository){
        this.repository=repository;
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
}
