package br.com.zup.proposta

import br.com.zup.client.proposta.PropostaClient
import br.com.zup.client.proposta.SolicitacaoAnalise
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

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

        var resultado = client.resultado(solicitacaoAnalise)

        proposta.analisarProposta(resultado)

        return proposta
    }

}
