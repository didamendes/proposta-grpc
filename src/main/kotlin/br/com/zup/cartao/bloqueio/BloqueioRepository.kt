package br.com.zup.cartao.bloqueio

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface BloqueioRepository: JpaRepository<Bloqueio, Long>