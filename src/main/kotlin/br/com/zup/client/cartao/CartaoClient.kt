package br.com.zup.client.cartao

import br.com.zup.cartao.model.Cartao
import br.com.zup.client.cartao.model.SolicitacaoAvisoViagem
import br.com.zup.client.cartao.model.SolicitacaoBloqueio
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${feign.url}")
interface CartaoClient {

    @Get(uri = "/cartoes")
    fun consultar(@QueryValue("idProposta") idProposta: String): Cartao

    @Post(uri = "/cartoes/{id}/bloqueios")
    fun bloqueiar(@PathVariable("id") id: String, @Body solicitacaoBloqueio: SolicitacaoBloqueio)

    @Post(uri = "/cartoes/{id}/avisos")
    fun avisoViagem(@PathVariable("id") id: String, @Body solicitacaoAvisoViagem: SolicitacaoAvisoViagem)

}