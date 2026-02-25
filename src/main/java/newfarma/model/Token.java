package newfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    public enum tokentipe{
        BEARER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idtoken;
    @Column(unique = true)
    private String token;
    @Enumerated(EnumType.STRING)
    public tokentipe tokenTipe= Token.tokentipe.BEARER;

    public boolean revoked;
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_idusuario")
    public Usuario user;
}
