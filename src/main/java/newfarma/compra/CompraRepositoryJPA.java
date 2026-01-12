package newfarma.compra;

import newfarma.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepositoryJPA extends JpaRepository<Compra,Long> {
    public boolean existsCompraByCorrelativo(String correlativo);
    public boolean existsCompraBySerieAndAndCorrelativo(String correlativo,String serie);
}