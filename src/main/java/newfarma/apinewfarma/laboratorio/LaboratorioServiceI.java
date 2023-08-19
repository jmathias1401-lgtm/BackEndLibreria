package newfarma.apinewfarma.laboratorio;

import newfarma.apinewfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListResponse;

public interface LaboratorioServiceI {
    LaboratorioListResponse list(LaboratorioListRequest params);
}
