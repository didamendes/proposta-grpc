package br.com.zup.proposta

import br.com.zup.*
import br.com.zup.shared.interceptors.ErrorAdvice
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Validator

@ErrorAdvice
@Singleton
class PropostaEndpoint(@Inject val validator: Validator, @Inject val service: PropostaService) :
    PropostaGrpcServiceGrpc.PropostaGrpcServiceImplBase() {

    override fun cadastrar(request: PropostaRequest, responseObserver: StreamObserver<PropostaResponse>) {
        val nova = request.toModel(validator)

        service.registrar(nova)

        responseObserver.onNext(PropostaResponse.newBuilder().setId(nova.id.toString()).build())
        responseObserver.onCompleted()
    }

    override fun consultar(
        request: ConsultarPropostaRequest,
        responseObserver: StreamObserver<ConsultarPropostaResponse>
    ) {
        val proposta = service.consultarPorId(request.id)

        responseObserver.onNext(
            ConsultarPropostaResponse.newBuilder().setId(proposta.id!!).setDocumento(proposta.documento)
                .setEmail(proposta.email).setEndereco(proposta.endereco).setNome(proposta.nome)
                .setSalario(proposta.salario.toString()).setStatus(proposta.statusSolicitacao!!.name).build()
        )
        responseObserver.onCompleted()
    }

}