package br.com.zup.proposta

import br.com.zup.PropostaGrpcServiceGrpc
import br.com.zup.PropostaRequest
import br.com.zup.PropostaResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Validator

@Singleton
class PropostaClient(@Inject val validator: Validator, @Inject val service: PropostaService) :
    PropostaGrpcServiceGrpc.PropostaGrpcServiceImplBase() {

    override fun cadastrar(request: PropostaRequest, responseObserver: StreamObserver<PropostaResponse>) {
        val nova = request.toModel(validator)

        service.registrar(nova)

        responseObserver.onNext(PropostaResponse.newBuilder().setId(nova.id.toString()).build())
        responseObserver.onCompleted()
    }

}