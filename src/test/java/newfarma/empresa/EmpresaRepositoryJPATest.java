package newfarma.empresa;

import newfarma.model.empresa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - EmpresaRepositoryJPA")
class EmpresaRepositoryJPATest {

    @Autowired
    private EmpresaRepositoryJPA empresaRepositoryJPA;

    @Test
    @DisplayName("findByNombre retorna empresas con el nombre especificado")
    void findByNombre_ShouldReturnEmpresas() {
        empresa emp = new empresa();
        emp.setNombre("EMPRESA_NOMBRE_" + System.currentTimeMillis());
        emp.setDireccion("Direccion Test");
        empresaRepositoryJPA.save(emp);

        List<empresa> result = empresaRepositoryJPA.findByNombre(emp.getNombre());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNombre()).isEqualTo(emp.getNombre());
    }

    @Test
    @DisplayName("existsEmpresaByNombre retorna true si la empresa existe")
    void existsEmpresaByNombre_ShouldReturnTrueWhenExists() {
        empresa emp = new empresa();
        emp.setNombre("EMPRESA_EXIST_" + System.currentTimeMillis());
        empresaRepositoryJPA.save(emp);

        boolean exists = empresaRepositoryJPA.existsEmpresaByNombre(emp.getNombre());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsEmpresaByNombre retorna false si no existe")
    void existsEmpresaByNombre_ShouldReturnFalseWhenNotExists() {
        boolean exists = empresaRepositoryJPA.existsEmpresaByNombre("EMPRESA_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("listEmpresasById retorna empresa por ID")
    void listEmpresasById_ShouldReturnEmpresa() {
        empresa emp = new empresa();
        emp.setNombre("EMPRESA_ID_" + System.currentTimeMillis());
        empresa saved = empresaRepositoryJPA.save(emp);

        List<empresa> list = empresaRepositoryJPA.listEmpresasById(saved.getIdempresa());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getIdempresa()).isEqualTo(saved.getIdempresa());
    }

    @Test
    @DisplayName("CountEmpresa retorna conteo no nulo")
    void countEmpresa_ShouldReturnCount() {
        Integer count = empresaRepositoryJPA.CountEmpresa();

        assertThat(count).isNotNull();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        empresa emp = new empresa();
        emp.setNombre("EMPRESA_CRUD_" + System.currentTimeMillis());
        emp.setCorreo("empresa@test.com");
        empresa saved = empresaRepositoryJPA.save(emp);

        Optional<empresa> found = empresaRepositoryJPA.findById(saved.getIdempresa());

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo(emp.getNombre());
        assertThat(found.get().getCorreo()).isEqualTo("empresa@test.com");
    }
}
