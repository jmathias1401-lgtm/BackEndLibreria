package newfarma.compra;

import newfarma.model.Compra;
import newfarma.compra.dto.CompraListRequest;
import newfarma.compra.dto.CompraListResponse;
import newfarma.utils.BaseResponse;

public interface CompraServiceI {
    CompraListResponse list(CompraListRequest params);

    BaseResponse save(Compra compra);

    BaseResponse eliminar(Long id);
}