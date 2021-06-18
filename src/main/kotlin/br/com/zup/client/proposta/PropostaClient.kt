package br.com.zup.client.proposta

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("\${url.solicitacao}")
interface PropostaClient {

    @Post(uri = "/solicitacao")
    fun resultado(@Body solicitacaoAnalise: SolicitacaoAnalise): ResultaAnalise

}