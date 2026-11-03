package newfarma.cargo;

import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.cargo.dto.CargoRequestDto;
import newfarma.model.Cargo;
import newfarma.utils.BaseResponse;

public interface CargoServiceI {
    CargoListResponse list(CargoListRequest params);

    BaseResponse save(CargoRequestDto cargoDto);

    BaseResponse save(Cargo cargo);

    BaseResponse eliminar(Long id);
}
