package br.com.zup.cartao.carteira

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface CarteiraRepository: JpaRepository<Carteira, Long> {
    fun existsByEmailAndEmissor(email: String, emissor: Tipo): Boolean
}