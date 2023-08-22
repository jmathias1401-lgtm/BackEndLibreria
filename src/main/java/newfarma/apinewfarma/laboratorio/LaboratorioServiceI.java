package newfarma.apinewfarma.laboratorio;

import newfarma.apinewfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.utils.BaseResponse;

public interface LaboratorioServiceI {
    LaboratorioListResponse list(LaboratorioListRequest params);

    BaseResponse save(Laboratorio laboratorio);
}
