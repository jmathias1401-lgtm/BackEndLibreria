package newfarma.estado;

import newfarma.estado.dto.EstadoListRequest;
import newfarma.estado.dto.EstadoListResponse;
import newfarma.model.Estado;
import newfarma.utils.BaseResponse;

public interface EstadoServiceI {
    EstadoListResponse list(EstadoListRequest params);

    BaseResponse save(Estado estado);

    BaseResponse eliminar(Long id);
}