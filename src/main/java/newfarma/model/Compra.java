package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="compra")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Compra extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcompra;
    private String serie;
    private String correlativo;
    private Date fechacompra;
    private Double costocompra;

    @ManyToOne
    @JoinColumn
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn
    private Empleado empleado;
    @ManyToOne
    @JoinColumn
    private TipoComprobante tipocomprobante;

}
