package newfarma.kardex;

import jakarta.persistence.EntityNotFoundException;
import newfarma.kardex.dto.KardexListResponse;
import newfarma.kardex.dto.KardexRequestDto;
import newfarma.model.Kardex;
import newfarma.model.Producto;
import newfarma.productos.ProductoRepositoryJPA;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class KardexService implements KardexServiceI {
    private final KardexRepositoryJPA kardexRepositoryJPA;
    private final ProductoRepositoryJPA productoRepositoryJPA;

    public KardexService(KardexRepositoryJPA kardexRepositoryJPA, ProductoRepositoryJPA productoRepositoryJPA) {
        this.kardexRepositoryJPA = kardexRepositoryJPA;
        this.productoRepositoryJPA = productoRepositoryJPA;
    }

    @Override
    public KardexListResponse listByProducto(Long idProducto) {
        List<Kardex> list = kardexRepositoryJPA.findByProductoId(idProducto);
        return KardexListResponse.builder()
                .total(list.size())
                .list(list)
                .build();
    }

    @Override
    public KardexListResponse listAuditoria(String tipoOperacion, String usuario, String search) {
        List<Kardex> list = kardexRepositoryJPA.findAuditoriaFiltrada(
                (tipoOperacion != null && !tipoOperacion.isBlank() && !tipoOperacion.equalsIgnoreCase("TODOS")) ? tipoOperacion.trim() : null,
                (usuario != null && !usuario.isBlank()) ? usuario.trim() : null,
                (search != null && !search.isBlank()) ? search.trim() : null
        );
        return KardexListResponse.builder()
                .total(list.size())
                .list(list)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(KardexRequestDto dto) {
        if (dto.getIdProducto() == null) {
            throw new IllegalArgumentException("El idProducto es obligatorio para registrar en kardex");
        }

        Producto producto = productoRepositoryJPA.findById(dto.getIdProducto())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + dto.getIdProducto()));

        Kardex kardex = Kardex.builder()
                .producto(producto)
                .fecha(LocalDateTime.now(ZoneId.of("America/Lima")))
                .tipoOperacion(dto.getTipoOperacion() != null ? dto.getTipoOperacion() : "AJUSTE")
                .motivo(dto.getMotivo() != null ? dto.getMotivo() : "Ajuste de inventario")
                .cantidad(dto.getCantidad() != null ? dto.getCantidad() : 0)
                .stockAnterior(dto.getStockAnterior() != null ? dto.getStockAnterior() : 0)
                .stockNuevo(dto.getStockNuevo() != null ? dto.getStockNuevo() : 0)
                .usuario(dto.getUsuario() != null ? dto.getUsuario() : "Administrador")
                .build();

        kardexRepositoryJPA.save(kardex);

        return BaseResponse.builder()
                .status(200)
                .code(String.valueOf(HttpStatus.OK))
                .message("Movimiento de kardex registrado exitosamente en base de datos")
                .build();
    }
}

