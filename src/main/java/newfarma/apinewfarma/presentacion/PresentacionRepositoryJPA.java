package newfarma.apinewfarma.presentacion;

import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PresentacionRepositoryJPA extends JpaRepository<Presentacion,Long> {
    @Query(value = "from Presentacion p where p.idpresentacion = ?1 ")
    public List<Presentacion> findByCodigoproducto(Integer codigo);
    public boolean existsLaboratorioByNombrepresentacion(String nombre);

}
