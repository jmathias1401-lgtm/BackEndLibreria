package newfarma.empresa;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.empresa;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.empresa.dto.EmpresaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService implements EmpresaServiceI{

    private final EmpresaRepository repository;
    EmpresaRepositoryJPA empresaRepositoryJPA;

    public EmpresaService(EmpresaRepositoryJPA empresaRepositoryJPA,EmpresaRepository repository){
        this.empresaRepositoryJPA=empresaRepositoryJPA;
        this.repository=repository;
    }

    @Override
    public EmpresaListResponse list(EmpresaListRequest params) {
        EmpresaListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil( (page-1) * xpage )+1;
        params.setOffset(offset-1);
        List<empresa> l = (List<empresa>)repository.list(params,"L");
        Long total = (Long) repository.list(params,"T");
        response = EmpresaListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }
    @Override
    public EmpresaListResponse EmpresaListById(Long id){
        EmpresaListResponse response;
        List<empresa> listEmpresas = empresaRepositoryJPA.listEmpresasById(id);

        response =EmpresaListResponse.builder()
                .page(listEmpresas.size())
                .total(listEmpresas.size())
                .xpage(listEmpresas.size())
                .list(listEmpresas)
                .build();
        return response;
    }
    @Override
    public BaseResponse save(empresa empresa){
        BaseResponse response;
        empresa empresa1;
        if(empresa.getIdempresa()!=null && empresa.getIdempresa()!=0)//actualiza un objeto existente
        {
            empresa1=empresaRepositoryJPA.findById(empresa.getIdempresa()).get();
            empresa1.setNombre(empresa.getNombre());
            empresa1.setDireccion(empresa.getDireccion());
            empresa1.setPaginaweb(empresa.getPaginaweb());
            empresa1.setTelefono(empresa.getTelefono());
            empresa1.setCorreo(empresa.getCorreo());
            empresa1.setInfo1(empresa.getInfo1());
            empresa1.setInfo2(empresa.getInfo2());
            empresa1.setInfo3(empresa.getInfo3());
            empresaRepositoryJPA.save(empresa1);

            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        }else//crea un nuevo objeto
        {
            boolean existName =empresaRepositoryJPA.existsEmpresaByNombre(empresa.getNombre());
            if (!existName){
                empresaRepositoryJPA.save(empresa);
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
        empresa empresa=empresaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (empresa.getIdempresa()!=null)
        {
            empresaRepositoryJPA.delete(empresa);
            response= BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        }else {
            response= BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }
    @Override
    public int CountEmpresa()
    {
        return empresaRepositoryJPA.CountEmpresa();
    }
}