package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="persona")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpersona;
    private Long dni;
    private Long ruc;
    private String nombre;
    private String materno;
    private String paterno;
    private Date fechanacimiento;
    private String telefono;
    private String correo;
    private String sexo;
    private String direccion;

    @ManyToOne
    @JoinColumn(name="tipopersona_idtipopersona")
    private TipoPersona tipoPersona;
}
