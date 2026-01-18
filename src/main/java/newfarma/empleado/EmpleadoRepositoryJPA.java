package newfarma.empleado;

import newfarma.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepositoryJPA extends JpaRepository<Empleado, Long> {
    public boolean existsEmpleadoByIdempleado(Long id);
}