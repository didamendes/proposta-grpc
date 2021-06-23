package br.com.zup.cartao.viagem

import br.com.zup.cartao.model.Cartao
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
class AvisoViagem(
    val validoAte: LocalDate,
    val destino: String,
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