package br.com.zup.client.cartao.model

class ResultadoCarteira(val id: String, val resultado: ResultadoEnum) {
    enum class ResultadoEnum {
        ASSOCIADA, FALHA
    }
}