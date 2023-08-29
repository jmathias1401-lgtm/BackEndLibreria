package newfarma.model;

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
    private Long idpresentacion;
    private String nombrepresentacion;
}
