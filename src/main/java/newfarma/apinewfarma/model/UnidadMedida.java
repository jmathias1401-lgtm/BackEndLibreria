package newfarma.apinewfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="unidadmedida")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idunidadmedida;
    private String nombreunidad;
}
