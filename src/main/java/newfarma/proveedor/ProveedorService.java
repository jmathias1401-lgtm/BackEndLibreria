package newfarma.proveedor;

import jakarta.persistence.EntityNotFoundException;
import newfarma.proveedor.dto.ProveedorListRequest;
import newfarma.proveedor.dto.ProveedorResponse;

import newfarma.model.Proveedor;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProveedorService implements ProveedorServiceI {
    private final ProveedorRepository repository;
    private final ProveedorRepositoryJPA repositoryJPA;
    public ProveedorService(ProveedorRepository repository, ProveedorRepositoryJPA repositoryJPA){
        this.repository=repository;
        this.repositoryJPA=repositoryJPA;
    }
    @Override
    public ProveedorResponse list(ProveedorListRequest params) {
        ProveedorResponse response;

        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<Proveedor> l = (List<Proveedor>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = ProveedorResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(Proveedor proveedor) {
        BaseResponse response;
        Proveedor proveedor1;
        if(proveedor.getIdproveedor()!=null)//actualiza un objeto existente
        {
            proveedor1=repositoryJPA.findById(proveedor.getIdproveedor()).get();
            proveedor1.setPersona(proveedor.getPersona());
            repositoryJPA.save(proveedor1);

            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            repositoryJPA.save(proveedor);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY").build();
        }

        return response;
    }
    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Proveedor proveedor=repositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (proveedor.getIdproveedor()!=null)
        {
            repositoryJPA.delete(proveedor);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
}