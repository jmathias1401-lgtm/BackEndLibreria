package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="empleado")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idempleado;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "estado_idestado")
    private Estado estado;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "persona_idpersona")
    private Persona persona;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_idusuario")
    private Usuario usuario;
}
