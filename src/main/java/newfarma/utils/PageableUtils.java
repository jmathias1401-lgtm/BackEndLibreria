package newfarma.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utilidades para estandarizar y proteger la paginación con Spring Data Pageable.
 * Convierte de forma segura parámetros 1-based (del frontend Angular) a Pageable de Spring (0-based)
 * y protege contra valores nulos o tamaños de página excesivos.
 */
public final class PageableUtils {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 500;

    private PageableUtils() {
    }

    /**
     * Construye un Pageable seguro a partir de page y xpage (1-based).
     *
     * @param page  Número de página (1-based)
     * @param xpage Elementos por página
     * @return Pageable válido y protegido
     */
    public static Pageable createPageable(Integer page, Integer xpage) {
        return createPageable(page, xpage, Sort.unsorted());
    }

    /**
     * Construye un Pageable seguro con ordenamiento.
     *
     * @param page  Número de página (1-based)
     * @param xpage Elementos por página
     * @param sort  Criterio de ordenamiento
     * @return Pageable válido y protegido
     */
    public static Pageable createPageable(Integer page, Integer xpage, Sort sort) {
        int safePage = (page == null || page < 1) ? DEFAULT_PAGE : page;
        int safeSize = (xpage == null || xpage < 1) ? DEFAULT_SIZE : Math.min(xpage, MAX_SIZE);
        Sort safeSort = (sort == null) ? Sort.unsorted() : sort;

        return PageRequest.of(safePage - 1, safeSize, safeSort);
    }
}

