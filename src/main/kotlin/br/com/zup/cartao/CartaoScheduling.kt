package br.com.zup.cartao

import br.com.zup.client.cartao.CartaoClient
import br.com.zup.cartao.model.CartaoRepository
import br.com.zup.proposta.PropostaRepository
import br.com.zup.proposta.model.StatusSolicitacao
import io.micronaut.scheduling.annotation.Scheduled
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class CartaoScheduling(
    @Inject val propostaRepository: PropostaRepository,
    @Inject val cartaoClient: CartaoClient,
    @Inject val cartaoRepository: CartaoRepository
) {

    @Scheduled(fixedDelay = "180s", initialDelay = "120s")
    @Transactional
    open fun associar() {
        println("Scheduling ai ai ai ui ui ui.")
        val propostas =
            propostaRepository.findAllByStatusSolicitacaoAndCartaoIsNull(StatusSolicitacao.ELEGIVEL)

        if (propostas.isNotEmpty()) {
            propostas.forEach { proposta ->
                try {
                    val cartao = cartaoClient.consultar(proposta.id.toString())
                    proposta.also { it.cartao = cartao }
                    cartaoRepository.save(cartao)
                    propostaRepository.update(proposta)
                } catch (e: Exception) {
                    println("Error ${e.message}")
                }
            }
        }

    }

}