package newfarma.empleado;

import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.empleado.dto.EmpleadoListResponse;
import newfarma.model.Empleado;
import newfarma.utils.BaseResponse;

public interface EmpleadoServiceI {
    EmpleadoListResponse list(EmpleadoListRequest params);

    BaseResponse save(Empleado empleado);

    BaseResponse eliminar(Long id);
}