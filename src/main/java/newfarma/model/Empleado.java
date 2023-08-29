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
    @ManyToOne
    @JoinColumn(name = "estado_idestado")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "persona_idpersona")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "usuario_idusuario")
    private Usuario usuario;



}
