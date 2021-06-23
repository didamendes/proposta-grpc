package br.com.zup.cartao.viagem

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface AvisoViagemRepository: JpaRepository<AvisoViagem, Long>