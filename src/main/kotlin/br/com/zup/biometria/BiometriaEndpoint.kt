package br.com.zup.biometria

import br.com.zup.BiometriaGrpcServiceGrpc
import br.com.zup.BiometriaRequest
import br.com.zup.BiometriaResponse
import br.com.zup.cartao.model.CartaoRepository
import br.com.zup.shared.exception.CartaoNaoEncontradaException
import br.com.zup.shared.interceptors.ErrorAdvice
import io.grpc.stub.StreamObserver
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ErrorAdvice
@Singleton
class BiometriaEndpoint(
    @Inject val cartaoRepository: CartaoRepository,
    @Inject val biometriaRepository: BiometriaRepository
) : BiometriaGrpcServiceGrpc.BiometriaGrpcServiceImplBase() {

    override fun cadastrar(request: BiometriaRequest, responseObserver: StreamObserver<BiometriaResponse>) {
        val cartao = cartaoRepository.findById(request.idCartao)
            .orElseThrow { CartaoNaoEncontradaException("Cartao nao encontrado !!") }

        val biometria = Biometria(Base64.getDecoder().decode(request.biometria), cartao)

        biometriaRepository.save(biometria)

        responseObserver.onNext(BiometriaResponse.newBuilder().setId(biometria.id!!).build())
        responseObserver.onCompleted()
    }

}