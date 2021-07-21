package br.com.zupacademy.sergio.proposta.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [Base64Validator::class])
annotation class Base64(
  val message: String = "must be a valid base64 encoded string",
  val groups: Array<KClass<Any>> = [],
  val payload: Array<KClass<Payload>> = []
)

class Base64Validator : ConstraintValidator<Base64, Any> {
  override fun isValid(
    value: Any, constraintValidatorContext: ConstraintValidatorContext
  ): Boolean =
    value is String && value.length > 0 && value.matches(
      Regex(
        pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$"
      )
    )
}
