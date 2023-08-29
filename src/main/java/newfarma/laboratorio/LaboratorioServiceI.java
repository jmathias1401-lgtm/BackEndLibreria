package newfarma.laboratorio;

import newfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.model.Laboratorio;
import newfarma.utils.BaseResponse;

public interface LaboratorioServiceI {
    LaboratorioListResponse list(LaboratorioListRequest params);

    BaseResponse save(Laboratorio laboratorio);

    BaseResponse eliminar(Long id);
}
