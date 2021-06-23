package br.com.zup.client.cartao.model

class ResultadoBloqueio(val resultado: Resultado)

enum class Resultado {
    BLOQUEADO, FALHA
}
