package newfarma.empresa;

import newfarma.model.empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpresaRepositoryJPA extends JpaRepository<empresa,Long> {
    @Query(value = "from empresa e where e.nombre = ?1 ")
    public List<empresa> findByNombre(String nombre);
    public boolean existsEmpresaByNombre(String nombre);

    @Query( value = "select * from empresa e where e.idEmpresa =?1",  nativeQuery = true)
    public List<empresa> listEmpresasById(Long empresaId);

    @Query(value = "select count(*) from empresa",nativeQuery = true)
    public Integer CountEmpresa();

}