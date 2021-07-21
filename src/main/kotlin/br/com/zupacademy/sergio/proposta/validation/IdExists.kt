package br.com.zupacademy.sergio.proposta.validation

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [IdExistsValidator::class])
annotation class IdExists(
  val message: String = "must exist",
  val entityClass: KClass<*>,
  val groups: Array<KClass<Any>> = [],
  val payload: Array<KClass<Payload>> = []
)

class IdExistsValidator : ConstraintValidator<IdExists, Any> {

  @PersistenceContext
  private lateinit var entityManager: EntityManager
  private lateinit var entityClass: KClass<*>

  override fun initialize(idExists: IdExists) {
    this.entityClass = idExists.entityClass
  }

  override fun isValid(
    value: Any, constraintValidatorContext: ConstraintValidatorContext
  ): Boolean = this.query(value).resultList.isNotEmpty()

  private fun query(value: Any) =
    this.entityManager
      .createQuery(this.queryClause())
      .setParameter("value", value)

  private fun queryClause(): String =
    "SELECT 1 FROM ${this.entityClass.qualifiedName} c WHERE id = :value"
}
