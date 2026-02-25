package newfarma.empresa;
import newfarma.model.empresa;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.empresa.dto.EmpresaListResponse;
import newfarma.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//@CrossOrigin(origins = "*")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/empresa")
//@CrossOrigin(origins = {"http:localhost:4200","http:localhost:8000"})
public class EmpresaController {
    EmpresaServiceI serviceI;
    EmpresaRepositoryJPA empresaRepositoryJPA;
    public EmpresaController(EmpresaServiceI serviceI,EmpresaRepositoryJPA empresaRepositoryJPA){
        this.serviceI=serviceI;
        this.empresaRepositoryJPA=empresaRepositoryJPA;
    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<EmpresaListResponse>list(EmpresaListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EmpresaListResponse>listbyId(@PathVariable Long id){
        return new ResponseEntity(serviceI.EmpresaListById(id), HttpStatus.OK);
    }


    @PostMapping
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestBody empresa empresa )
    {
        //return new ResponseEntity("respuesta desde SAVE_EMPRESA", HttpStatus.OK);
        return new ResponseEntity(serviceI.save(empresa), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return new ResponseEntity(serviceI.eliminar(id),HttpStatus.OK);
    }


}