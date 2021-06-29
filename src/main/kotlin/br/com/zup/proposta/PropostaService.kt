package br.com.zup.proposta

import br.com.zup.client.proposta.PropostaClient
import br.com.zup.client.proposta.SolicitacaoAnalise
import br.com.zup.proposta.model.Proposta
import br.com.zup.proposta.model.StatusSolicitacao
import br.com.zup.shared.exception.PropostaNaoEncontradaException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@Singleton
class PropostaService(@Inject val repository: PropostaRepository, @Inject val client: PropostaClient) {

    @Transactional
     fun registrar(@Valid proposta: Proposta): Proposta {

        repository.save(proposta)
        var solicitacaoAnalise = SolicitacaoAnalise(
            documento = proposta.documento,
            nome = proposta.nome,
            idProposta = proposta.id.toString()
        )

        try {
            var resultado = client.resultado(solicitacaoAnalise)
            proposta.analisarProposta(resultado.body())
        } catch (e: Exception) {
            proposta.also { it.statusSolicitacao = StatusSolicitacao.NAO_ELEGIVEL }
        }

        return proposta
    }

    @Transactional
    fun consultarPorId(@NotNull id: Long): Proposta {
        val proposta = repository.findById(id).orElseThrow {throw PropostaNaoEncontradaException("Proposta nao encontrado") }

        return proposta
    }

}
