package newfarma.productos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.productos.dto.ProductoRequestDto;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/producto")
@Tag(name = "Productos", description = "Catálogo de productos farmacéuticos y de librería, inventario y gestión de imágenes")
public class ProductoController {
    private final ProductoServiceI serviceI;
    private final ProductoRepositoryJPA productoRepositoryJPA;

    public ProductoController(ProductoServiceI serviceI, ProductoRepositoryJPA productoRepositoryJPA) {
        this.serviceI = serviceI;
        this.productoRepositoryJPA = productoRepositoryJPA;
    }

    @Operation(summary = "Listar catálogo de productos", description = "Retorna el listado paginado y filtrable de productos con sus detalles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos listados exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductListResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ProductListResponse> list(ProductListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Consultar producto por ID", description = "Retorna la información detallada de un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductListResponse> listbyId(
            @Parameter(description = "Identificador numérico del producto", required = true, example = "1")
            @PathVariable int id) {
        return new ResponseEntity<>(serviceI.ProductListById(id), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar producto con imagen", description = "Crea o actualiza un producto aceptando datos en multipart/form-data y un archivo de imagen opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto registrado/actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida en datos del producto"),
            @ApiResponse(responseCode = "500", description = "Error interno o código de barra duplicado")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> save(
            @Valid @RequestPart("producto") ProductoRequestDto productoDto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return new ResponseEntity<>(serviceI.save(productoDto, imagen), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador del producto a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
