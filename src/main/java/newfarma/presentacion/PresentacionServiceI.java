package newfarma.presentacion;
import newfarma.model.Presentacion;
import newfarma.presentacion.dto.PresentacionListRequest;
import newfarma.presentacion.dto.PresentacionListResponse;
import newfarma.utils.BaseResponse;

public interface PresentacionServiceI {
    PresentacionListResponse list(PresentacionListRequest params);

    BaseResponse save(Presentacion presentacion);

    BaseResponse eliminar(Long id);
}
