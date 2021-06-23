package br.com.zup.cartao.model

import br.com.zup.proposta.model.Proposta
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
    var id: String? = null

    @Enumerated(EnumType.STRING)
    var status: Status = Status.ATIVO

    @OneToOne(mappedBy = "cartao")
    var proposta: Proposta? = null

    fun isBloqueado(): Boolean {
        return Status.BLOQUEADO.equals(status)
    }

    fun bloquear() {
        status = Status.BLOQUEADO
    }
}
