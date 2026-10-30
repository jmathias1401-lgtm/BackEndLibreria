package newfarma.cargo;

import newfarma.model.Cargo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - CargoRepositoryJPA")
class CargoRepositoryJPATest {

    @Autowired
    private CargoRepositoryJPA cargoRepositoryJPA;

    @Test
    @DisplayName("existsCargoByNombrecargo retorna true si el cargo existe por nombre")
    void existsCargoByNombrecargo_ShouldReturnTrueWhenExists() {
        Cargo cargo = new Cargo();
        cargo.setNombrecargo("CARGO_TEST_" + System.currentTimeMillis());
        cargo.setDescripcion("Descripcion de prueba");
        cargoRepositoryJPA.save(cargo);

        boolean exists = cargoRepositoryJPA.existsCargoByNombrecargo(cargo.getNombrecargo());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsCargoByNombrecargo retorna false si no existe")
    void existsCargoByNombrecargo_ShouldReturnFalseWhenNotExists() {
        boolean exists = cargoRepositoryJPA.existsCargoByNombrecargo("CARGO_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsCargoByIdcargo retorna true si el cargo existe por ID")
    void existsCargoByIdcargo_ShouldReturnTrueWhenExists() {
        Cargo cargo = new Cargo();
        cargo.setNombrecargo("CARGO_ID_" + System.currentTimeMillis());
        cargo.setDescripcion("Descripcion ID");
        Cargo saved = cargoRepositoryJPA.save(cargo);

        boolean exists = cargoRepositoryJPA.existsCargoByIdcargo(saved.getIdcargo().intValue());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Cargo cargo = new Cargo();
        cargo.setNombrecargo("CARGO_CRUD_" + System.currentTimeMillis());
        cargo.setDescripcion("CRUD Test");
        Cargo saved = cargoRepositoryJPA.save(cargo);

        Optional<Cargo> found = cargoRepositoryJPA.findById(saved.getIdcargo());

        assertThat(found).isPresent();
        assertThat(found.get().getNombrecargo()).isEqualTo(cargo.getNombrecargo());
    }
}
