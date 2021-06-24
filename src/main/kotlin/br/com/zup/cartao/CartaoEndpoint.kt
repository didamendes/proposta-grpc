package br.com.zup.cartao

import br.com.zup.*
import br.com.zup.shared.interceptors.ErrorAdvice
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorAdvice
@Singleton
class CartaoEndpoint(
    @Inject val cartaoService: CartaoService
) : CartaoGrpcServiceGrpc.CartaoGrpcServiceImplBase() {

    override fun bloquear(request: BloqueioRequest, responseObserver: StreamObserver<BloqueioResponse>) {

        val bloqueio = cartaoService.bloquear(request.idCartao)

        responseObserver.onNext(BloqueioResponse.newBuilder().setId(bloqueio.id!!).build())
        responseObserver.onCompleted()
    }

    override fun avisoViagem(request: ViagemRequest, responseObserver: StreamObserver<ViagemResponse>) {
        val avisarViagem = cartaoService.avisarViagem(request)

        responseObserver.onNext(ViagemResponse.newBuilder().setId(avisarViagem.id!!).build())
        responseObserver.onCompleted()
    }

    override fun associarCarteira(
        request: AssociarCarteiraRequest,
        responseObserver: StreamObserver<AssociarCarteiraResponse>
    ) {
        val associar = cartaoService.associar(request)

        responseObserver.onNext(AssociarCarteiraResponse.newBuilder().setId(associar.id!!).build())
        responseObserver.onCompleted()
    }

}