package newfarma.cargo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.cargo.dto.CargoRequestDto;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cargo")
@Tag(name = "Cargos", description = "Gestión de puestos y cargos laborales de la organización")
public class CargoController {
    private final CargoServiceI serviceI;
    private final CargoRepositoryJPA cargoRepositoryJPA;

    public CargoController(CargoServiceI serviceI, CargoRepositoryJPA cargoRepositoryJPA) {
        this.serviceI = serviceI;
        this.cargoRepositoryJPA = cargoRepositoryJPA;
    }

    @Operation(summary = "Listar cargos", description = "Obtiene una lista paginada y filtrada de cargos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cargos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CargoListResponse.class)))
    })
    @GetMapping
    public ResponseEntity<CargoListResponse> list(CargoListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Registrar o actualizar cargo", description = "Crea un nuevo cargo laboral o actualiza uno existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo procesado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de cargo inválidos"),
            @ApiResponse(responseCode = "500", description = "Nombre de cargo ya existe")
    })
    @PostMapping
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody CargoRequestDto cargoDto) {
        return new ResponseEntity<>(serviceI.save(cargoDto), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar cargo", description = "Elimina un cargo por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo eliminado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del cargo", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
