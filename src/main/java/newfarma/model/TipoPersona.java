package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tipopersona")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoPersona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtipopersona;
    private String nombre;

}
