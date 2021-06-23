package br.com.zup.proposta

import br.com.zup.ConsultarPropostaRequest
import br.com.zup.PropostaGrpcServiceGrpc
import br.com.zup.PropostaRequest
import br.com.zup.client.proposta.PropostaClient
import br.com.zup.client.proposta.ResultaAnalise
import br.com.zup.client.proposta.SolicitacaoAnalise
import br.com.zup.client.proposta.TipoResultadoSolicitacao
import br.com.zup.proposta.model.Proposta
import br.com.zup.proposta.model.StatusSolicitacao
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
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
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class PropostaEndpointTest(
    val client: PropostaGrpcServiceGrpc.PropostaGrpcServiceBlockingStub,
    val propostaRepository: PropostaRepository
) {

    @Inject
    lateinit var propostaClient: PropostaClient

    @BeforeEach
    internal fun setUp() {
        propostaRepository.deleteAll()
    }

    @AfterEach
    internal fun tearDown() {
        propostaRepository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar uma proposta`() {
        val propostaRequestMock = getPropostaRequestMock()
        val resultaAnaliseMock = getResultaAnaliseMock()
        val solicitacaoAnaliseMock = getSolicitacaoAnaliseMock()

        Mockito.`when`(propostaClient.resultado(solicitacaoAnaliseMock)).thenReturn(resultaAnaliseMock)

        val cadastrar = client.cadastrar(propostaRequestMock)

        assertNotNull(cadastrar.id)
        assertTrue(propostaRepository.existsById(cadastrar.id.toLong()))
    }

    @Test
    internal fun `deve cadastrar uma proposta elegivel`() {
        val propostaRequestMock = getPropostaRequestMock()
        val solicitacaoAnaliseMock = getSolicitacaoAnaliseMock()

        Mockito.`when`(propostaClient.resultado(solicitacaoAnaliseMock)).thenReturn(null)

        val cadastrar = client.cadastrar(propostaRequestMock)

        assertNotNull(cadastrar.id)
        assertTrue(propostaRepository.existsById(cadastrar.id.toLong()))
    }

    @Test
    internal fun `deve consultar uma proposta pelo id`() {
        val proposta = getProposta()
        propostaRepository.save(proposta)

        val consultar = client.consultar(ConsultarPropostaRequest.newBuilder().setId(1L).build())

        assertNotNull(consultar)
    }

    @Test
    internal fun `nao deve consultar uma proposta com id inexistente`() {

        val erro = assertThrows<StatusRuntimeException> {
            client.consultar(ConsultarPropostaRequest.newBuilder().setId(1L).build())
        }

        assertEquals(Status.NOT_FOUND.code, erro.status.code)
        assertEquals("Proposta nao encontrado", erro.status.description)
    }

    /**
     * Objetos mockados !!!!
     */
    fun getSolicitacaoAnaliseMock(): SolicitacaoAnalise {
        return SolicitacaoAnalise("47360345480", "Dove", "1")
    }

    fun getResultaAnaliseMock(): ResultaAnalise {
        return ResultaAnalise("47360345480", "Dove", "1", TipoResultadoSolicitacao.SEM_RESTRICAO)
    }

    fun getPropostaRequestMock(): PropostaRequest {
        return PropostaRequest.newBuilder().setDocumento("47360345480").setEmail("dove@zup.com.br").setNome("Dove")
            .setEndereco("Rua Joel").setSalario("5000").build()
    }

    fun getProposta(): Proposta {
        var proposta = Proposta("47360345480", "dove@zup.com.br", "Dove", "Rua Joel", BigDecimal(5000))
        proposta.statusSolicitacao = StatusSolicitacao.ELEGIVEL
        return proposta
    }

    @Factory
    class Clients {

        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PropostaGrpcServiceGrpc.PropostaGrpcServiceBlockingStub? {
            return PropostaGrpcServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(PropostaClient::class)
    fun propostaClienteMock(): PropostaClient {
        return Mockito.mock(PropostaClient::class.java)
    }
}