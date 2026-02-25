package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="cargo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcargo;
    private String nombrecargo;
    private String descripcion;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "estado_idestado")
    private Estado estado;

}
