package br.com.zup.util

import org.hibernate.SessionFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.Query
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Singleton
open class UniqueValueValidated(@Inject val entityManager: EntityManager, @Inject val sessionFactory: SessionFactory) :
    ConstraintValidator<UniqueValue, String> {

    lateinit var field: String
    lateinit var clazz: KClass<*>

    override fun initialize(constraintAnnotation: UniqueValue) {
        field = constraintAnnotation.fieldName.toLowerCase()
        clazz = constraintAnnotation.domainClass
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {

        val sesh = sessionFactory.openSession()
        val em = sesh.entityManagerFactory.createEntityManager()

        val query: Query = em.createQuery("SELECT 1 FROM ${clazz.simpleName} WHERE $field =:value")
        query.setParameter("value", value)
        val resultList = query.resultList

        sessionFactory.isClosed

        if (resultList.isNotEmpty()) {
            return false
        }

        return true
    }
}