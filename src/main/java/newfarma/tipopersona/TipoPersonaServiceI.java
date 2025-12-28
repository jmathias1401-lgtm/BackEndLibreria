package newfarma.tipopersona;

import newfarma.tipopersona.dto.TipoPersonaListRequest;
import newfarma.tipopersona.dto.TipoPersonaListResponse;
import newfarma.model.TipoPersona;
import newfarma.utils.BaseResponse;

public interface TipoPersonaServiceI {
    TipoPersonaListResponse list(TipoPersonaListRequest params);

    BaseResponse save(TipoPersona tipopersona);

    BaseResponse eliminar(Long id);
}
