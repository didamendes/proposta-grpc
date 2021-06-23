package br.com.zup.cartao

import br.com.zup.BloqueioRequest
import br.com.zup.CartaoGrpcServiceGrpc
import br.com.zup.ViagemRequest
import br.com.zup.cartao.bloqueio.BloqueioRepository
import br.com.zup.cartao.model.Cartao
import br.com.zup.cartao.model.CartaoRepository
import br.com.zup.cartao.model.Status
import br.com.zup.cartao.viagem.AvisoViagemRepository
import br.com.zup.client.cartao.CartaoClient
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CartaoEndpointTest(
    val bloqueioRepository: BloqueioRepository,
    val cartaoRepository: CartaoRepository,
    val avisoViagemRepository: AvisoViagemRepository,
    val cartaoGrpcServiceGrpc: CartaoGrpcServiceGrpc.CartaoGrpcServiceBlockingStub
) {

    @Inject
    lateinit var cartaoClient: CartaoClient

    @BeforeEach
    internal fun setUp() {
        avisoViagemRepository.deleteAll()
        bloqueioRepository.deleteAll()
        cartaoRepository.deleteAll()
    }

    @AfterEach
    internal fun tearDown() {
        avisoViagemRepository.deleteAll()
        bloqueioRepository.deleteAll()
        cartaoRepository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar um aviso de viagem`() {
        val avisoViagemRequestMock = getAvisoViagemRequestMock()
        val cartaoMock = getCartaoMock()
        cartaoRepository.save(cartaoMock)

        val response = cartaoGrpcServiceGrpc.avisoViagem(avisoViagemRequestMock)

        assertNotNull(response)
        assertTrue(avisoViagemRepository.existsById(response.id))
    }

    @Test
    internal fun `deve bloquear um cartao`() {

        val bloqueioRequestMock = getBloqueioRequestMock()
        val cartaoMock = getCartaoMock()
        cartaoRepository.save(cartaoMock)

        val response = cartaoGrpcServiceGrpc.bloquear(bloqueioRequestMock)

        assertNotNull(response)
        assertTrue(bloqueioRepository.existsById(response.id))
    }

    @Test
    internal fun `nao deve bloquear um cartao pois cartao ja existe`() {

        val bloqueioRequestMock = getBloqueioRequestMock()
        val cartaoMock = getCartaoMock()
        cartaoMock.apply { status = Status.BLOQUEADO }
        cartaoRepository.save(cartaoMock)

        val error = assertThrows<StatusRuntimeException> { cartaoGrpcServiceGrpc.bloquear(bloqueioRequestMock) }

        assertEquals(io.grpc.Status.INVALID_ARGUMENT.code, error.status.code)
        assertEquals("Cartão já bloqueado !", error.status.description)
    }

    /**
     * Objetos mockados
     *
     */
    fun getBloqueioRequestMock(): BloqueioRequest {
        return BloqueioRequest.newBuilder().setIdCartao("5002-6093-9673-1730").build()
    }

    fun getAvisoViagemRequestMock(): ViagemRequest {
        return ViagemRequest.newBuilder().setIdCartao("5002-6093-9673-1730").setValidoAte("2021-06-30")
            .setDestino("New York").build()
    }

    fun getCartaoMock(): Cartao {
        val cartao = Cartao("Dove", "1", LocalDateTime.now(), 10000)
        cartao.also { it.status = Status.ATIVO; it.id = "5002-6093-9673-1730" }
        return cartao
    }

    @Factory
    class Clients {

        @Replaces
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): CartaoGrpcServiceGrpc.CartaoGrpcServiceBlockingStub? {
            return CartaoGrpcServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(CartaoClient::class)
    fun cartaoClienteMock(): CartaoClient {
        return Mockito.mock(CartaoClient::class.java)
    }

}