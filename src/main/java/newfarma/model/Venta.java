package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="venta")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Venta extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idventa;
    private String serie;
    private String correlativo;
    private Date fechaventa;
    private Double igv;
    private Double subtotal;
    private Double costoventa;

    @ManyToOne
    @JoinColumn(name = "cliente_idcliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "empleado_idempleado")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "tipocomprobante_idtipocomprobante")
    private TipoComprobante tipoComprobante;

}
