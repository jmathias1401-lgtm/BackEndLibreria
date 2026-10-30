package newfarma.venta;

import newfarma.model.Venta;
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
@DisplayName("Pruebas de Repositorio - VentaRepositoryJPA")
class VentaRepositoryJPATest {

    @Autowired
    private VentaRepositoryJPA ventaRepositoryJPA;

    @Test
    @DisplayName("existsVentaByCorrelativo retorna true si la venta existe por correlativo")
    void existsVentaByCorrelativo_ShouldReturnTrueWhenExists() {
        Venta v = new Venta();
        v.setSerie("B001");
        v.setCorrelativo("V_CORR_" + System.currentTimeMillis());
        v.setFechaventa(new Date());
        v.setSubtotal(100.0);
        v.setIgv(18.0);
        v.setCostoventa(118.0);
        ventaRepositoryJPA.save(v);

        boolean exists = ventaRepositoryJPA.existsVentaByCorrelativo(v.getCorrelativo());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsVentaByCorrelativo retorna false si no existe")
    void existsVentaByCorrelativo_ShouldReturnFalseWhenNotExists() {
        boolean exists = ventaRepositoryJPA.existsVentaByCorrelativo("V_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Venta v = new Venta();
        v.setSerie("B002");
        v.setCorrelativo("V_CRUD_" + System.currentTimeMillis());
        v.setFechaventa(new Date());
        v.setCostoventa(75.50);
        Venta saved = ventaRepositoryJPA.save(v);

        Optional<Venta> found = ventaRepositoryJPA.findById(saved.getIdventa());

        assertThat(found).isPresent();
        assertThat(found.get().getCorrelativo()).isEqualTo(v.getCorrelativo());
        assertThat(found.get().getCostoventa()).isEqualTo(75.50);
    }
}
