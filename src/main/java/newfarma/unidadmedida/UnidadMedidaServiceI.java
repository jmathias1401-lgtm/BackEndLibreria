package newfarma.unidadmedida;
import newfarma.model.UnidadMedida;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.utils.BaseResponse;

public interface UnidadMedidaServiceI {
    UnidadMedidaListResponse list(UnidadMedidaListRequest params);

    BaseResponse save(UnidadMedida unidadMedida);

    BaseResponse eliminar(Integer id);
}
