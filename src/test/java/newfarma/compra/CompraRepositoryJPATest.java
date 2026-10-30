package newfarma.compra;

import newfarma.model.Compra;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - CompraRepositoryJPA")
class CompraRepositoryJPATest {

    @Autowired
    private CompraRepositoryJPA compraRepositoryJPA;

    @Test
    @DisplayName("existsCompraByCorrelativo retorna true si existe")
    void existsCompraByCorrelativo_ShouldReturnTrueWhenExists() {
        Compra c = new Compra();
        c.setSerie("F" + (System.currentTimeMillis() % 1000));
        c.setCorrelativo("C" + System.currentTimeMillis());
        c.setFechacompra(new Date());
        c.setCostocompra(150.0);
        compraRepositoryJPA.save(c);

        boolean exists = compraRepositoryJPA.existsCompraByCorrelativo(c.getCorrelativo());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsCompraByCorrelativo retorna false si no existe")
    void existsCompraByCorrelativo_ShouldReturnFalseWhenNotExists() {
        boolean exists = compraRepositoryJPA.existsCompraByCorrelativo("CORR_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsCompraBySerieAndAndCorrelativo retorna true si coinciden serie y correlativo")
    void existsCompraBySerieAndAndCorrelativo_ShouldReturnTrueWhenMatches() {
        Compra c = new Compra();
        c.setSerie("S" + (System.currentTimeMillis() % 1000));
        c.setCorrelativo("CORR_" + System.currentTimeMillis());
        c.setFechacompra(new Date());
        c.setCostocompra(200.0);
        compraRepositoryJPA.save(c);

        // Spring Data method name derivation binds BySerie (1st param) AndCorrelativo (2nd param)
        boolean exists = compraRepositoryJPA.existsCompraBySerieAndAndCorrelativo(c.getSerie(), c.getCorrelativo());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Compra c = new Compra();
        c.setSerie("F999");
        c.setCorrelativo("CORR_CRUD_" + System.currentTimeMillis());
        c.setFechacompra(new Date());
        c.setCostocompra(350.50);
        Compra saved = compraRepositoryJPA.save(c);

        Optional<Compra> found = compraRepositoryJPA.findById(saved.getIdcompra());

        assertThat(found).isPresent();
        assertThat(found.get().getCorrelativo()).isEqualTo(c.getCorrelativo());
        assertThat(found.get().getCostocompra()).isEqualTo(350.50);
    }
}
