package newfarma.cargo;

import jakarta.persistence.EntityNotFoundException;
import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.model.Cargo;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CargoService implements CargoServiceI {
    private final CargoRepository repository;
    private final CargoRepositoryJPA repositoryJPA;
    public CargoService(CargoRepository repository, CargoRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public CargoListResponse list(CargoListRequest params) {
        CargoListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Cargo> l = (List<Cargo>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = CargoListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Cargo cargo){
        BaseResponse response;
        Cargo cargo1;
        if(cargo.getIdcargo()!=null)//actualiza un objeto existente
        {
            cargo1=repositoryJPA.findById(cargo.getIdcargo()).get();
            cargo1.setNombrecargo(cargo.getNombrecargo());
            cargo1.setEstado(cargo.getEstado());
            cargo1.setDescripcion(cargo.getDescripcion());
            repositoryJPA.save(cargo1);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =repositoryJPA.existsCargoByNombrecargo(cargo.getNombrecargo());
            if (!existName){
                repositoryJPA.save(cargo);
                response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
            }else{
                response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
            }
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Cargo cargo=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (cargo.getIdcargo()!=null)
        {
            repositoryJPA.delete(cargo);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}
