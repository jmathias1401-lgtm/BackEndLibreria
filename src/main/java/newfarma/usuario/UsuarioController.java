package newfarma.usuario;
import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//@CrossOrigin(origins = "*")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/usuario")
//@CrossOrigin(origins = {"http:localhost:4200","http:localhost:8000"})
public class UsuarioController {
    UsuarioServiceI serviceI;
    UsuarioRepositoryJPA usuarioRepositoryJPA;
    public UsuarioController(UsuarioServiceI serviceI,UsuarioRepositoryJPA usuarioRepositoryJPA){
        this.serviceI=serviceI;
        this.usuarioRepositoryJPA=usuarioRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<UserListResponse>list(UserListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserListResponse>listbyId(@PathVariable Long id){
        return new ResponseEntity(serviceI.UserListById(id), HttpStatus.OK);
    }


    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody Usuario usuario )
    {
        //return new ResponseEntity("respuesta desde SAVE_USUARIO", HttpStatus.OK);
        return new ResponseEntity(serviceI.save(usuario), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }


}