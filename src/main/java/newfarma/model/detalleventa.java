package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="detalleventa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class detalleventa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iddetalleventa;
    private String codigodetalleventa;
    private Integer unidades;
    private Double costounidad;
    private Double subtotal;
    private Double descuentounidad;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "venta_idventa")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_idproducto")
    private Producto producto;
}