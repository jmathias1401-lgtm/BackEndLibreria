package newfarma.kardex;

import newfarma.kardex.dto.KardexListResponse;
import newfarma.kardex.dto.KardexRequestDto;
import newfarma.utils.BaseResponse;

public interface KardexServiceI {
    KardexListResponse listByProducto(Long idProducto);
    KardexListResponse listAuditoria(String tipoOperacion, String usuario, String search);
    BaseResponse save(KardexRequestDto dto);
}

