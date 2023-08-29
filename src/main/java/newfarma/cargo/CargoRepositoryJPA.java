package newfarma.cargo;

import newfarma.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepositoryJPA extends JpaRepository<Cargo,Long> {
    public boolean existsCargoByNombrecargo(String codigo);
    public boolean existsCargoByIdcargo(Integer id);
}
