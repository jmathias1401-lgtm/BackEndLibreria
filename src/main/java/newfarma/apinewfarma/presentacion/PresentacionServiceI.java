package newfarma.apinewfarma.presentacion;
import newfarma.apinewfarma.model.Presentacion;
import newfarma.apinewfarma.presentacion.dto.PresentacionListRequest;
import newfarma.apinewfarma.presentacion.dto.PresentacionListResponse;
import newfarma.apinewfarma.utils.BaseResponse;

public interface PresentacionServiceI {
    PresentacionListResponse list(PresentacionListRequest params);

    BaseResponse save(Presentacion presentacion);

    BaseResponse eliminar(Long id);
}
