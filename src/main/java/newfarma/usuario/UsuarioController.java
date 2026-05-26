package newfarma.usuario;

import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    private final UsuarioServiceI serviceI;
    private final UsuarioRepositoryJPA usuarioRepositoryJPA;

    public UsuarioController(UsuarioServiceI serviceI, UsuarioRepositoryJPA usuarioRepositoryJPA) {
        this.serviceI = serviceI;
        this.usuarioRepositoryJPA = usuarioRepositoryJPA;
    }

    @GetMapping
    public ResponseEntity<UserListResponse> list(UserListRequest params) {
        return new ResponseEntity<>(serviceI.list(params), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserListResponse> listbyId(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.UserListById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(serviceI.save(usuario), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(serviceI.eliminar(id), HttpStatus.OK);
    }
}