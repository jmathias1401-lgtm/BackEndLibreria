package newfarma.productos;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import newfarma.laboratorio.LaboratorioService;
import newfarma.model.Laboratorio;
import newfarma.model.Presentacion;
import newfarma.model.Producto;
import newfarma.model.UnidadMedida;
import newfarma.presentacion.PresentacionService;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.productos.dto.ProductoRequestDto;
import newfarma.unidadmedida.UnidadMedidaService;
import newfarma.utils.BaseResponse;
import newfarma.utils.PageableUtils;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductoService implements ProductoServiceI {
    @Value("${app.ruta-imagenes}")
    private String rutaBaseImagenes;

    private final ProductoRepository repository;
    ProductoRepositoryJPA productoRepositoryJPA;
    LaboratorioService laboratorioService;
    PresentacionService presentacionService;
    UnidadMedidaService unidadMedidaService;

    public ProductoService(ProductoRepositoryJPA productoRepositoryJPA, ProductoRepository repository,
            LaboratorioService laboratorioService, PresentacionService presentacionService,
            UnidadMedidaService unidadMedidaService) {
        this.productoRepositoryJPA = productoRepositoryJPA;
        this.repository = repository;
        this.laboratorioService = laboratorioService;
        this.presentacionService = presentacionService;
        this.unidadMedidaService = unidadMedidaService;
    }

    @Override
    public ProductListResponse list(ProductListRequest params) {
        Pageable pageable = PageableUtils.createPageable(params.getPage(), params.getXpage());
        params.setPage(pageable.getPageNumber() + 1);
        params.setXpage(pageable.getPageSize());
        params.setOffset((int) pageable.getOffset());

        List<Producto> l = (List<Producto>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        return ProductListResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .total(total)
                .xpage(pageable.getPageSize())
                .list(l)
                .build();
    }

    @Override
    public ProductListResponse ProductListById(int id) {
        List<Producto> listProductos = productoRepositoryJPA.listProductosById(id);
        return ProductListResponse.builder()
                .page(listProductos.size())
                .total(listProductos.size())
                .xpage(listProductos.size())
                .list(listProductos)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(ProductoRequestDto producto, MultipartFile imagen) {
        String dateParse = formatearFecha(producto.getVencimiento());
        boolean isUpdate = producto.getIdproducto() != null && producto.getIdproducto() != 0;
        Producto productoTarget;

        if (isUpdate) {
            productoTarget = productoRepositoryJPA.findById(producto.getIdproducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + producto.getIdproducto()));
        } else {
            if (productoRepositoryJPA.existsProductoByCodigoproducto(producto.getCodigoproducto())) {
                return BaseResponse.builder()
                        .status(500)
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                        .message("NOT SUCESS")
                        .build();
            }
            productoTarget = new Producto();
        }

        mapearCamposProducto(productoTarget, producto, dateParse, imagen);
        productoRepositoryJPA.save(productoTarget);

        String mensaje = isUpdate ? "UPDATE SUCESSFULLY" : "SAVED SUCESSFULLY";
        return BaseResponse.builder()
                .status(200)
                .code(String.valueOf(HttpStatus.OK))
                .message(mensaje)
                .build();
    }

    private void mapearCamposProducto(Producto target, ProductoRequestDto source, String dateParse, MultipartFile imagen) {
        target.setCodigoproducto(source.getCodigoproducto());
        target.setNombre(source.getNombre());
        target.setVencimiento(dateParse);
        target.setEstado(source.getEstado());
        target.setComposicion(source.getComposicion());
        target.setUbicacion(source.getUbicacion());
        target.setStock(source.getStock());
        target.setPrecioventa(source.getPrecioventa());
        target.setPrecioblister(source.getPrecioblister());
        target.setPreciocaja(source.getPreciocaja());
        target.setCodbarra(source.getCodbarra());

        if (source.getIdpresentacion() != null && source.getIdpresentacion() != 0) {
            Presentacion pres = new Presentacion();
            pres.setIdpresentacion(source.getIdpresentacion());
            target.setPresentacion(pres);
        }
        if (source.getIdunidadmedida() != null && source.getIdunidadmedida() != 0) {
            UnidadMedida um = new UnidadMedida();
            um.setIdunidadmedida(source.getIdunidadmedida());
            target.setUnidadmedida(um);
        }
        if (source.getIdlaboratorio() != null && source.getIdlaboratorio() != 0) {
            Laboratorio lab = new Laboratorio();
            lab.setIdlaboratorio(source.getIdlaboratorio());
            target.setLaboratorio(lab);
        }

        String nombreImagen = guardarImagenProducto(imagen, source.getImagen_path());
        if (nombreImagen != null) {
            target.setImagen_path(nombreImagen);
        }
    }

    private String formatearFecha(String date) {
        if (date != null && date.contains("-")) {
            String[] partes = date.split("-");
            if (partes.length == 3) {
                return partes[2] + "/" + partes[1] + "/" + partes[0];
            }
        }
        return date;
    }

    private String guardarImagenProducto(MultipartFile imagen, String nombreImagen) {
        if (imagen == null || imagen.isEmpty()) {
            return null;
        }

        try {
            Path carpetaBase = Paths.get(rutaBaseImagenes).toAbsolutePath().normalize();
            Files.createDirectories(carpetaBase);

            String nombreFinal = (nombreImagen != null && !nombreImagen.trim().isEmpty())
                    ? nombreImagen
                    : imagen.getOriginalFilename();

            if (nombreFinal == null || nombreFinal.trim().isEmpty()) {
                nombreFinal = "producto_" + UUID.randomUUID() + ".png";
            }

            Path pathLimpio = Paths.get(nombreFinal).getFileName();
            String nombreArchivoLimpio = (pathLimpio != null) ? pathLimpio.toString() : ("img_" + UUID.randomUUID() + ".png");

            Path rutaArchivo = carpetaBase.resolve(nombreArchivoLimpio).normalize();
            if (!rutaArchivo.startsWith(carpetaBase)) {
                throw new SecurityException("Intento de acceso a ruta no permitida: " + nombreImagen);
            }

            try (var inputStream = imagen.getInputStream()) {
                Files.copy(inputStream, rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            }

            return nombreArchivoLimpio;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen en el disco: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse eliminar(Long id) {
        Producto producto = productoRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (producto.getIdproducto() != null) {
            productoRepositoryJPA.delete(producto);
            return BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK))
                    .message("DELETE SUCESSFULLY").build();
        } else {
            return BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .message("NOT SUCESS").build();
        }
    }

    @Override
    public int CountProducto() {
        return productoRepositoryJPA.CountProduct();
    }
}
