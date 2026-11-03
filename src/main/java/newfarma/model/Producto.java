package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Producto extends AuditableEntity {
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
    private String imagen_path;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "laboratorio_idlaboratorio")
    private Laboratorio laboratorio;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "presentacion_idpresentacion")
    private Presentacion presentacion;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "unidadmedida_idunidadmedida")
    private UnidadMedida unidadmedida;

}
