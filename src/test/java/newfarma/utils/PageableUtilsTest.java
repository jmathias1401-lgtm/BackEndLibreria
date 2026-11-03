package newfarma.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias - PageableUtils y PagedResponse")
class PageableUtilsTest {

    @Test
    @DisplayName("createPageable con parámetros normales convierte 1-based a 0-based")
    void createPageable_WithValidParams() {
        Pageable pageable = PageableUtils.createPageable(1, 20);

        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getOffset()).isEqualTo(0);
    }

    @Test
    @DisplayName("createPageable para segunda página calcula offset correctamente")
    void createPageable_PageTwo_CalculatesOffset() {
        Pageable pageable = PageableUtils.createPageable(2, 25);

        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(25);
        assertThat(pageable.getOffset()).isEqualTo(25);
    }

    @Test
    @DisplayName("createPageable con null aplica valores por defecto seguros")
    void createPageable_WithNulls_UsesDefaults() {
        Pageable pageable = PageableUtils.createPageable(null, null);

        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(PageableUtils.DEFAULT_SIZE);
    }

    @Test
    @DisplayName("createPageable con números negativos o cero aplica valores seguros")
    void createPageable_WithNegativeOrZero_UsesDefaults() {
        Pageable pageable = PageableUtils.createPageable(0, -10);

        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(PageableUtils.DEFAULT_SIZE);
    }

    @Test
    @DisplayName("createPageable con tamaño excesivo se limita a MAX_SIZE para evitar DoS")
    void createPageable_WithExcessiveSize_CapsToMaxSize() {
        Pageable pageable = PageableUtils.createPageable(1, 5000);

        assertThat(pageable.getPageSize()).isEqualTo(PageableUtils.MAX_SIZE);
    }

    @Test
    @DisplayName("createPageable respeta el criterio de ordenamiento Sort")
    void createPageable_WithSort() {
        Sort sort = Sort.by("nombre").ascending();
        Pageable pageable = PageableUtils.createPageable(1, 10, sort);

        assertThat(pageable.getSort()).isEqualTo(sort);
    }

    @Test
    @DisplayName("PagedResponse.fromPage convierte Page de Spring a contrato 1-based de Frontend")
    void pagedResponse_FromPage_MapsCorrectly() {
        List<String> items = List.of("Item 1", "Item 2");
        Page<String> springPage = new PageImpl<>(items, PageRequest.of(0, 10), 50);

        PagedResponse<String> response = PagedResponse.fromPage(springPage);

        assertThat(response.getPage()).isEqualTo(1); // 1-based
        assertThat(response.getXpage()).isEqualTo(10);
        assertThat(response.getTotal()).isEqualTo(50L);
        assertThat(response.getList()).containsExactly("Item 1", "Item 2");
    }
}

