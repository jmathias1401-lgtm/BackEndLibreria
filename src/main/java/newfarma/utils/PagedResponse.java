package newfarma.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Respuesta paginada estándar y genérica.
 * Compatible con la estructura esperada por el cliente Angular (total, xpage, page, list)
 * y construible directamente desde un Page<T> de Spring Data JPA.
 *
 * @param <T> Tipo de elemento de la lista
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PagedResponse<T> {
    private Object total;
    private Integer xpage;
    private Integer page;
    private List<T> list;

    /**
     * Construye una PagedResponse a partir de una instancia Page de Spring Data.
     */
    public static <T> PagedResponse<T> fromPage(Page<T> springPage) {
        return PagedResponse.<T>builder()
                .total(springPage.getTotalElements())
                .xpage(springPage.getSize())
                .page(springPage.getNumber() + 1) // Convertir 0-based a 1-based para el frontend
                .list(springPage.getContent())
                .build();
    }
}

