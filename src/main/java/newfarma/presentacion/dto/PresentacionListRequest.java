package newfarma.presentacion.dto;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PresentacionListRequest {
    Integer page;
    Integer xpage;
    Integer offset;
    String nombre;
    Integer idpresentacion;

}
