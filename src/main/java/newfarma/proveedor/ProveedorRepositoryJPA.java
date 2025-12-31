package newfarma.proveedor;


import newfarma.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepositoryJPA extends JpaRepository<Proveedor,Long> {

    public boolean existsProveedorByIdproveedor(Integer id);
}