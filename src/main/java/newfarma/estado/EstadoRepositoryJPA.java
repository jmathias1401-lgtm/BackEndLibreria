package newfarma.estado;

import newfarma.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepositoryJPA extends JpaRepository<Estado,Long> {
    public boolean existsEstadoByEstado(String estado);
    public boolean existsEstadoByIdestado(Long id);
}