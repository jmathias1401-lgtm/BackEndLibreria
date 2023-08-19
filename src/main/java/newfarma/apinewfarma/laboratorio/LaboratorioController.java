package newfarma.apinewfarma.laboratorio;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.apinewfarma.laboratorio.dto.LaboratorioListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("api/laboratorio")
class LaboratorioController {
    LaboratorioServiceI serviceI;
    LaboratorioRepositoryJPA laboratorioRepositoryJPA;
    public LaboratorioController(LaboratorioServiceI serviceI){
        this.serviceI=serviceI;

    }
    @GetMapping
    @ResponseBody
    public ResponseEntity<LaboratorioListResponse>list(LaboratorioListRequest params){
        return new ResponseEntity(serviceI.list(params), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
