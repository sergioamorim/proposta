package br.com.zupacademy.sergio.proposta.validation

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueValueValidator::class])
annotation class UniqueValue(
  val message: String = "must be unique",
  val domainClass: KClass<*>,
  val fieldName: String,
  val groups: Array<KClass<Any>> = [],
  val payload: Array<KClass<Payload>> = []
)

class UniqueValueValidator : ConstraintValidator<UniqueValue, Any> {

  @PersistenceContext
  private lateinit var entityManager: EntityManager
  private lateinit var domainClass: KClass<*>
  private lateinit var fieldName: String

  override fun initialize(uniqueValue: UniqueValue) {
    this.domainClass = uniqueValue.domainClass
    this.fieldName = uniqueValue.fieldName
  }

  override fun isValid(
    fieldValue: Any, constraintValidatorContext: ConstraintValidatorContext
  ): Boolean =
    this.query(fieldValue = fieldValue).resultList.isEmpty()

  private fun query(fieldValue: Any): Query =
    this.entityManager
      .createQuery(this.queryClause())
      .setParameter("fieldValue", fieldValue)

  private fun queryClause(): String =
    "SELECT 1 FROM ${this.domainClass.qualifiedName} c " +
    "WHERE ${this.fieldName} = :fieldValue"
}
