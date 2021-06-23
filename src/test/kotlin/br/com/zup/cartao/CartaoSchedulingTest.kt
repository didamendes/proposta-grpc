package br.com.zup.cartao

import br.com.zup.cartao.model.Cartao
import br.com.zup.cartao.model.Status
import br.com.zup.client.cartao.CartaoClient
import br.com.zup.proposta.PropostaRepository
import br.com.zup.proposta.model.Proposta
import br.com.zup.proposta.model.StatusSolicitacao
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class CartaoSchedulingTest(val propostaRepository: PropostaRepository,
                val cartaoScheduling: CartaoScheduling) {

    @Inject
    lateinit var cartaoClient: CartaoClient

    @Test
    internal fun `deve associar um cartao ao proposta ELEGIVEL`() {
        val cartaoMock = getCartaoMock()
        val proposta = getPropostaMock()
        propostaRepository.save(proposta)
        cartaoMock.also { it.proposta = proposta; it.id = "5002-6093-9673-1730" }

        Mockito.`when`(cartaoClient.consultar("1")).thenReturn(cartaoMock)

        cartaoScheduling.associar()
    }

    fun getPropostaMock(): Proposta {
        var proposta = Proposta("47360345480", "dove@zup.com.br", "Dove", "Rua Joel", BigDecimal(5000))
        proposta.also { it.statusSolicitacao = StatusSolicitacao.ELEGIVEL }
        return proposta
    }

    fun getCartaoMock(): Cartao {
        val cartao = Cartao("Dove", "1", LocalDateTime.now(), 10000)
        cartao.also { it.status = Status.ATIVO }
        return cartao
    }

    @MockBean(CartaoClient::class)
    fun cartaoClienteMock(): CartaoClient {
        return Mockito.mock(CartaoClient::class.java)
    }

}