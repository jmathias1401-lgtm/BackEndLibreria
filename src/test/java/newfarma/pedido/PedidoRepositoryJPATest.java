package newfarma.pedido;

import newfarma.model.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - PedidoRepositoryJPA")
class PedidoRepositoryJPATest {

    @Autowired
    private PedidoRepositoryJPA pedidoRepositoryJPA;

    @Test
    @DisplayName("lastPedido ejecuta query del último código de pedido")
    void lastPedido_ShouldReturnLastCode() {
        Pedido p = new Pedido();
        p.setCodigopedido("PED-999999");
        p.setFechapedido(new Date());
        p.setStock(5);
        pedidoRepositoryJPA.save(p);

        List<String> result = pedidoRepositoryJPA.lastPedido();

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Pedido p = new Pedido();
        p.setCodigopedido("PED-TEST-" + System.currentTimeMillis());
        p.setFechapedido(new Date());
        p.setStock(8);
        Pedido saved = pedidoRepositoryJPA.save(p);

        Optional<Pedido> found = pedidoRepositoryJPA.findById(saved.getIdpedido());

        assertThat(found).isPresent();
        assertThat(found.get().getCodigopedido()).isEqualTo(p.getCodigopedido());
        assertThat(found.get().getStock()).isEqualTo(8);
    }
}
