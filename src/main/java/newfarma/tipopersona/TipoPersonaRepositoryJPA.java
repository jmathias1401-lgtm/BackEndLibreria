package newfarma.tipopersona;

import newfarma.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPersonaRepositoryJPA extends JpaRepository<TipoPersona,Long> {
    //@Query(value = "from TipoPersona p where p.idtipopersona = ?1 ")
    //public List<TipoPersona> findByCodigoproducto(Integer codigo);
    public boolean existsTipoPersonaByNombre(String codigo);
    public boolean existsTipoPersonaByIdtipopersona(Integer id);
}
