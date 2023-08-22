package newfarma.apinewfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="presentacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpresentacion;
    private String nombrepresentacion;
}
