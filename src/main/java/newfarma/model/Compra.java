package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="compra")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcompra;
    private String serie;
    private String correlativo;
    private Date fechacompra;
    private Double costocompra;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private Proveedor proveedor;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private Empleado empleado;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private TipoComprobante tipocomprobante;

}