package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tipocomprobante")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoComprobante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtipocomprobante;
    private String nombrecomprobante;
}
