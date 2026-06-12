package newfarma.productos;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import newfarma.laboratorio.LaboratorioService;
import newfarma.model.Producto;
import newfarma.presentacion.PresentacionService;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.unidadmedida.UnidadMedidaService;
import newfarma.utils.BaseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
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
        ProductListResponse response;
        int page = params.getPage();
        int xpage = params.getXpage();
        int offset = (int) Math.ceil((page - 1) * xpage) + 1;
        params.setOffset(offset - 1);
        List<Producto> l = (List<Producto>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        response = ProductListResponse.builder()
                .page(Integer.valueOf(params.getPage().toString()))
                .total(total)
                .xpage(Integer.valueOf(params.getXpage().toString()))
                .list(l)
                .build();
        return response;
    }

    @Override
    public ProductListResponse ProductListById(int id) {
        ProductListResponse response;
        List<Producto> listProductos = productoRepositoryJPA.listProductosById(id);

        response = ProductListResponse.builder()
                .page(listProductos.size())
                .total(listProductos.size())
                .xpage(listProductos.size())
                .list(listProductos)
                .build();
        return response;
    }

    @Override
    public BaseResponse save(Producto producto, MultipartFile imagen) {
        String date = producto.getVencimiento();
        String[] partes = date.split("-");
        String dateParse = partes[2] + "/" + partes[1] + "/" + partes[0];
        producto.setVencimiento(dateParse);
        BaseResponse response;
        Producto producto1;
        if (producto.getIdproducto() != null && producto.getIdproducto() != 0)// actualiza un objeto existente
        {
            producto1 = productoRepositoryJPA.findById(producto.getIdproducto()).get();
            producto1.setCodigoproducto(producto.getCodigoproducto());
            producto1.setNombre(producto.getNombre());
            producto1.setVencimiento(producto.getVencimiento());
            producto1.setEstado(producto.getEstado());
            producto1.setComposicion(producto.getComposicion());
            producto1.setUbicacion(producto.getUbicacion());
            producto1.setStock(producto.getStock());
            producto1.setPrecioventa(producto.getPrecioventa());
            producto1.setPrecioblister(producto.getPrecioblister());
            producto1.setPreciocaja(producto.getPreciocaja());
            producto1.setCodbarra(producto.getCodbarra());
            producto1.setPresentacion(producto.getPresentacion());
            producto1.setUnidadmedida(producto.getUnidadmedida());
            producto1.setLaboratorio(producto.getLaboratorio());

            String nombreImagen = guardarImagenProducto(imagen, producto.getImagen_path());
            if (nombreImagen != null) {
                producto1.setImagen_path(nombreImagen);
            }

            productoRepositoryJPA.save(producto1);

            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK))
                    .message("UPDATE SUCESSFULLY").build();
        } else// crea un nuevo objeto
        {
            boolean existName = productoRepositoryJPA.existsProductoByCodigoproducto(producto.getCodigoproducto());
            if (!existName) {
                String nombreImagen = guardarImagenProducto(imagen, producto.getImagen_path());
                producto.setImagen_path(nombreImagen);
                productoRepositoryJPA.save(producto);

                response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK))
                        .message("SAVED SUCESSFULLY").build();
            } else {
                response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                        .message("NOT SUCESS").build();
            }
        }

        return response;
    }

    private String guardarImagenProducto(MultipartFile imagen, String nombreImagen) {
        if (imagen == null || imagen.isEmpty()) {
            return null;
        }

        try {
            // 1. Crear carpeta base utilizando los imports limpios
            Path carpetaBase = Paths.get(rutaBaseImagenes);
            Files.createDirectories(carpetaBase);

            String nombreArchivo = nombreImagen;
            Path rutaArchivo = carpetaBase.resolve(nombreArchivo);
            try (var inputStream = imagen.getInputStream()) {
                Files.copy(inputStream, rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            }

            return nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen en el disco: " + e.getMessage(), e);
        }
    }

   

    @Override
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Producto producto = productoRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (producto.getIdproducto() != null) {
            productoRepositoryJPA.delete(producto);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK))
                    .message("DELETE SUCESSFULLY").build();
        } else {
            response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .message("NOT SUCESS").build();
        }
        return response;
    }

    @Override
    public int CountProducto() {
        return productoRepositoryJPA.CountProduct();
    }
}
