package br.com.zup.shared.interceptors

import io.micronaut.aop.Around

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Around
annotation class ErrorAdvice
