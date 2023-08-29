package newfarma.unidadmedida;

import newfarma.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadMedidaRepositoryJPA extends JpaRepository<UnidadMedida,Long> {
    //@Query(value = "from UnidadMedida p where p.idunidadmedida = ?1 ")
    public boolean existsUnidadMedidaByNombreunidad(String nombre);
}
