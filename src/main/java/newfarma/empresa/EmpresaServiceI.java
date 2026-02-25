package newfarma.empresa;

import newfarma.model.empresa;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.empresa.dto.EmpresaListResponse;
import newfarma.utils.BaseResponse;

public interface EmpresaServiceI {
    EmpresaListResponse list(EmpresaListRequest params);

    public EmpresaListResponse EmpresaListById(Long id);

    BaseResponse save(empresa empresa);

    BaseResponse eliminar(Long id);

    int CountEmpresa();
}