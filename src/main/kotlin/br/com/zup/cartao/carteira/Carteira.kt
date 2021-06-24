package br.com.zup.cartao.carteira

import br.com.zup.cartao.model.Cartao
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
class Carteira(
    val email: String,
    @Enumerated(EnumType.STRING)
    val emissor: Tipo,
    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    val cartao: Cartao
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    val dataCriacao: OffsetDateTime? = null

}