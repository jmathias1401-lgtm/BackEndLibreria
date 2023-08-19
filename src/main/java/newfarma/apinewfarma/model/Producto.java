package newfarma.apinewfarma.model;

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
    private Integer idproducto;
    private String codigoproducto;
    private String nombre;
    private String vencimiento;
    private String estado;
    private String composicion;
    private String ubicacion;
    private Integer presentacion_idpresentacion;
    private Integer unidadmedida_idunidadmedida;
    private Integer laboratorio_idlaboratorio;
    private Integer stock;
    private Integer precioventa;
    private Integer precioblister;
    private Integer preciocaja;
    private String codbarra;
}
