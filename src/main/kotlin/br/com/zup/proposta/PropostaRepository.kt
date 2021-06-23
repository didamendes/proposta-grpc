package br.com.zup.proposta

import br.com.zup.proposta.model.Proposta
import br.com.zup.proposta.model.StatusSolicitacao
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PropostaRepository: JpaRepository<Proposta, Long> {

    fun findAllByStatusSolicitacaoAndCartaoIsNull(statusSolicitacao: StatusSolicitacao): MutableList<Proposta>

}