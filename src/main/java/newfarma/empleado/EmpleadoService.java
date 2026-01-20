package newfarma.empleado;

import jakarta.persistence.EntityNotFoundException;
import newfarma.empleado.dto.EmpleadoListRequest;
import newfarma.empleado.dto.EmpleadoListResponse;
import newfarma.model.Empleado;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService implements EmpleadoServiceI {
    private final EmpleadoRepository repository;
    private final EmpleadoRepositoryJPA repositoryJPA;

    public EmpleadoService(EmpleadoRepository repository, EmpleadoRepositoryJPA repositoryJPA) {
        this.repository = repository;
        this.repositoryJPA = repositoryJPA;
    }

    @Override
    public EmpleadoListResponse list(EmpleadoListRequest params) {
        EmpleadoListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil((page - 1) * xpage) + 1;
        params.setOffset(offset - 1);
        List<Empleado> l = (List<Empleado>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        response = EmpleadoListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }

    @Override
    public BaseResponse save(Empleado empleado) {
        BaseResponse response;
        Empleado empleado1;
        if (empleado.getIdempleado() != null)//actualiza un objeto existente
        {
            empleado1 = repositoryJPA.findById(empleado.getIdempleado()).get();
            empleado1.setEstado(empleado.getEstado());
            empleado1.setPersona(empleado.getPersona());
            empleado1.setUsuario(empleado.getUsuario());
            repositoryJPA.save(empleado1);

            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        } else//crea un nuevo objeto
        {
            repositoryJPA.save(empleado);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
        }

        return response;
    }

    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Empleado empleado = repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (empleado.getIdempleado() != null) {
            repositoryJPA.delete(empleado);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        } else {
            response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}