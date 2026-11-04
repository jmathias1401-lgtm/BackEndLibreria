package newfarma.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario")
@Tag(name = "Usuarios", description = "Administración de cuentas de usuario, roles y accesos al sistema")
public class UsuarioController {
    private final UsuarioServiceI serviceI;
    private final UsuarioRepositoryJPA usuarioRepositoryJPA;

    public UsuarioController(UsuarioServiceI serviceI, UsuarioRepositoryJPA usuarioRepositoryJPA) {
        this.serviceI = serviceI;
        this.usuarioRepositoryJPA = usuarioRepositoryJPA;
    }

    @Operation(summary = "Listar usuarios", description = "Obtiene una lista paginada y filtrada de usuarios del sistema")
    @GetMapping
    public ResponseEntity<UserListResponse> list(UserListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @Operation(summary = "Consultar usuario por ID", description = "Obtiene los datos de un usuario por su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<UserListResponse> listbyId(
            @Parameter(description = "Identificador único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.UserListById(id), HttpStatus.OK);
    }

    @Operation(summary = "Guardar o actualizar usuario", description = "Crea un nuevo usuario o actualiza sus credenciales y estado")
    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(serviceI.save(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina una cuenta de usuario del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(
            @Parameter(description = "Identificador único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}
