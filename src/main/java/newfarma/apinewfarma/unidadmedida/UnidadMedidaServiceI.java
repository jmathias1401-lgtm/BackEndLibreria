package newfarma.apinewfarma.unidadmedida;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.apinewfarma.unidadmedida.dto.UnidadMedidaListResponse;

public interface UnidadMedidaServiceI {
    UnidadMedidaListResponse list(UnidadMedidaListRequest params);
}
