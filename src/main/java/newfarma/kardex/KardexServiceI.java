package newfarma.kardex;

import newfarma.kardex.dto.KardexListResponse;
import newfarma.kardex.dto.KardexRequestDto;
import newfarma.utils.BaseResponse;

public interface KardexServiceI {
    KardexListResponse listByProducto(Long idProducto);
    BaseResponse save(KardexRequestDto dto);
}

