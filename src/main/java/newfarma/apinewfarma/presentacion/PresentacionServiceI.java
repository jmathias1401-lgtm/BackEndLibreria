package newfarma.apinewfarma.presentacion;
import newfarma.apinewfarma.presentacion.dto.PresentacionListRequest;
import newfarma.apinewfarma.presentacion.dto.PresentacionListResponse;

public interface PresentacionServiceI {
    PresentacionListResponse list(PresentacionListRequest params);
}
