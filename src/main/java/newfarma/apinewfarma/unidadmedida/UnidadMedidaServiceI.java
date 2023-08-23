package newfarma.apinewfarma.unidadmedida;
import newfarma.apinewfarma.model.UnidadMedida;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListResponse;
import newfarma.apinewfarma.utils.BaseResponse;

public interface UnidadMedidaServiceI {
    UnidadMedidaListResponse list(UnidadMedidaListRequest params);

    BaseResponse save(UnidadMedida unidadMedida);

    BaseResponse eliminar(Long id);
}
