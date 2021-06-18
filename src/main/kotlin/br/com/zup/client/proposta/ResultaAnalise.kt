package br.com.zup.client.proposta

data class ResultaAnalise(
    val documento: String,
    val nome: String,
    val idProposta: String,
    val resultadoSolicitacao: TipoResultadoSolicitacao
)
