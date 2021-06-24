package br.com.zup.cartao.model

import br.com.zup.cartao.carteira.Carteira
import br.com.zup.cartao.carteira.Tipo
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

    @OneToMany(mappedBy = "cartao")
    lateinit var carteiras: Set<Carteira>

    fun isBloqueado(): Boolean {
        return Status.BLOQUEADO.equals(status)
    }

    fun bloquear() {
        status = Status.BLOQUEADO
    }

    fun isAssociado(tipo: Tipo): Boolean {
        val filter: List<Carteira> = carteiras.filter { carteira -> carteira.emissor.equals(tipo) }
        return filter.isNotEmpty()
    }
}
