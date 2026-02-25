package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="detallecompra")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class detallecompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iddetallecompra;
    private String codigodetallecompra;
    private String lote;
    private Integer unidades;
    private Double costounidad;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "compra_idcompra")
    private Compra compra;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "producto_idproducto")
    private Producto producto;
}