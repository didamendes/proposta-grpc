package br.com.zup.proposta

import br.com.zup.PropostaRequest
import br.com.zup.proposta.model.Proposta
import java.math.BigDecimal
import javax.validation.ConstraintViolationException
import javax.validation.Validator

fun PropostaRequest.toModel(validator: Validator): Proposta {
    val proposta =
        Proposta(documento = documento, email = email, nome = nome, endereco = endereco, salario = BigDecimal(salario))

    val erros = validator.validate(proposta)

    if (erros.isNotEmpty()) {
        throw ConstraintViolationException(erros)
    }

    return proposta
}