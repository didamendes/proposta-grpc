package br.com.zup.client.cartao.model

class ResultadoAvisoViagem(resultado: ResultadoViagem) {
    enum class ResultadoViagem {
        CRIADO, FALHA
    }
}