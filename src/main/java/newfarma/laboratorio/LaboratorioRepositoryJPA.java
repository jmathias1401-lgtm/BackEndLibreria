package newfarma.laboratorio;

import newfarma.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioRepositoryJPA extends JpaRepository<Laboratorio,Long> {
    //@Query(value = "from Laboratorio p where p.idlaboratorio = ?1 ")
    //public List<Laboratorio> findByCodigoproducto(Integer codigo);
    public boolean existsLaboratorioByNombrelaboratorio(String codigo);
    public boolean existsLaboratorioByIdlaboratorio(Integer id);
}
