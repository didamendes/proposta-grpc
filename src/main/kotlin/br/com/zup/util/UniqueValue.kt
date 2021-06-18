package br.com.zup.util

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@ReportAsSingleViolation // the error reports of each individual composing constraint are ignored
@Constraint(validatedBy = [ UniqueValueValidated::class ]) // we don't need a validator :-)
@Documented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class UniqueValue(
    val message: String = "documento ja existe",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
    val domainClass: KClass<*>,
    val fieldName: String
)
