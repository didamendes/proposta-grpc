package br.com.zup.cartao

import br.com.zup.proposta.Proposta
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Cartao(
    val titular: String,
    val idProposta: String,
    val emitidoEm: LocalDateTime,
    val limite: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    private val status: Status = Status.ATIVO

    @OneToOne(mappedBy = "cartao")
    private val proposta: Proposta? = null
}
