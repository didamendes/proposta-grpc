package br.com.zup.proposta

import br.com.zup.cartao.Cartao
import br.com.zup.client.proposta.ResultaAnalise
import br.com.zup.client.proposta.TipoResultadoSolicitacao
import br.com.zup.util.CPFouCNPJ
import br.com.zup.util.UniqueValue
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Entity
class Proposta(
    @field:NotBlank @field:CPFouCNPJ
    @field:UniqueValue(domainClass = Proposta::class, fieldName = "documento")
    val documento: String,

    @field:NotBlank @field:Email
    @field:UniqueValue(domainClass = Proposta::class, fieldName = "email", message = "Email ja existe")
    val email: String,

    @field:NotBlank val nome: String,
    @field:NotBlank val endereco: String,
    @field:Positive val salario: BigDecimal
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    var statusSolicitacao: StatusSolicitacao? = null

    @OneToOne()
    @JoinColumn(name = "cartao_id", referencedColumnName = "id")
    var cartao: Cartao? = null

    fun analisarProposta(resultado: ResultaAnalise) {
        if (TipoResultadoSolicitacao.COM_RESTRICAO.equals(resultado.resultadoSolicitacao)) {
            statusSolicitacao = StatusSolicitacao.NAO_ELEGIVEL
        } else {
            statusSolicitacao = StatusSolicitacao.ELEGIVEL
        }
    }

}