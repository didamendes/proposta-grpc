package br.com.zup.cartao

import br.com.zup.AssociarCarteiraRequest
import br.com.zup.ViagemRequest
import br.com.zup.biometria.BiometriaRepository
import br.com.zup.cartao.bloqueio.Bloqueio
import br.com.zup.cartao.bloqueio.BloqueioRepository
import br.com.zup.cartao.carteira.Carteira
import br.com.zup.cartao.carteira.CarteiraRepository
import br.com.zup.cartao.carteira.Tipo
import br.com.zup.cartao.model.CartaoRepository
import br.com.zup.cartao.viagem.AvisoViagem
import br.com.zup.cartao.viagem.AvisoViagemRepository
import br.com.zup.client.cartao.CartaoClient
import br.com.zup.client.cartao.model.SolicitacaoAvisoViagem
import br.com.zup.client.cartao.model.SolicitacaoBloqueio
import br.com.zup.client.cartao.model.SolicitacaoInclusaoCarteira
import br.com.zup.shared.exception.CartaoJaBloqueadoException
import br.com.zup.shared.exception.CartaoNaoEncontradaException
import br.com.zup.shared.exception.CarteiraExistenteException
import io.micronaut.validation.Validated
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@Validated
@Singleton
class CartaoService(
    @Inject val cartaoRepository: CartaoRepository,
    @Inject val bloqueioRepository: BloqueioRepository,
    @Inject val cartaoClient: CartaoClient,
    @Inject val carteiraRepository: CarteiraRepository,
    @Inject val avisoViagemRepository: AvisoViagemRepository
) {

    @Transactional
    fun bloquear(@NotBlank idCartao: String): Bloqueio {
        val cartao = cartaoRepository.findById(idCartao)
            .orElseThrow { CartaoNaoEncontradaException("Cartao nao encontrado") }

        if (cartao.isBloqueado()) {
            throw CartaoJaBloqueadoException("Cartão já bloqueado !")
        }

        val bloqueio = Bloqueio(cartao = cartao)

        cartaoClient.bloqueiar(cartao.id!!, SolicitacaoBloqueio("Proposta"))
        cartao.bloquear()
        bloqueioRepository.save(bloqueio)

        return bloqueio
    }

    fun avisarViagem(request: ViagemRequest): AvisoViagem {
        val cartao = cartaoRepository.findById(request.idCartao)
            .orElseThrow { CartaoNaoEncontradaException("Cartão nao encontrado") }

        val avisoViagem =
            AvisoViagem(validoAte = converterDate(request.validoAte), destino = request.destino, cartao = cartao)

        cartaoClient.avisoViagem(
            cartao.id!!,
            SolicitacaoAvisoViagem(avisoViagem.destino, avisoViagem.validoAte.toString())
        )
        avisoViagemRepository.save(avisoViagem)

        return avisoViagem
    }

    @Transactional
    fun associar(request: AssociarCarteiraRequest): Carteira {
        val cartao = cartaoRepository.findById(request.idCartao)
            .orElseThrow { CartaoNaoEncontradaException("Cartão nao encontrado") }

        val tipo = if (request.tipo == br.com.zup.Tipo.PAYPAL) {
            Tipo.PAYPAL
        } else {
            Tipo.SAMSUNG_PAY
        }

        if (carteiraRepository.existsByEmailAndEmissor(request.email, tipo) || cartao.isAssociado(tipo)) {
            throw CarteiraExistenteException("Carteira ja existente")
        }

        val carteira = Carteira(request.email, tipo, cartao)
        cartaoClient.associar(request.idCartao, SolicitacaoInclusaoCarteira(carteira.email, carteira.emissor.name))
        carteiraRepository.save(carteira)


        return carteira
    }

    private fun converterDate(validoAte: String?): LocalDate {
        return LocalDate.parse(validoAte, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

}
