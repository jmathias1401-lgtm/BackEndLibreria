package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproducto;
    private String codigoproducto;
    private String nombre;
    private String vencimiento;
    private String estado;
    private String composicion;
    private String ubicacion;
    private Integer stock;
    private Double precioventa;
    private Double precioblister;
    private Double preciocaja;
    private String codbarra;

    @ManyToOne
    @JoinColumn(name = "laboratorio_idlaboratorio")
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "presentacion_idpresentacion")
    private Presentacion presentacion;

    @ManyToOne
    @JoinColumn(name = "unidadmedida_idunidadmedida")
    private UnidadMedida unidadMedida;

}
