package newfarma.apinewfarma.unidadmedida;

import newfarma.apinewfarma.model.Laboratorio;
import newfarma.apinewfarma.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UnidadMedidaRepositoryJPA extends JpaRepository<Laboratorio,Long> {
    @Query(value = "from UnidadMedida p where p.idunidadmedida = ?1 ")
    public List<UnidadMedida> findByCodigoproducto(Integer codigo);
}
