package br.com.zupacademy.sergio.proposal.validation

import javax.servlet.http.HttpServletRequest
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Retention
@Target(AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [UserAgentNotBlankValidator::class])
annotation class UserAgentNotBlank(
  val message: String = "user agent must not be null, empty or blank",
  val groups: Array<KClass<Any>> = [],
  val payload: Array<KClass<Payload>> = []
)

class UserAgentNotBlankValidator : ConstraintValidator<UserAgentNotBlank, Any> {
  override fun isValid(any: Any, p1: ConstraintValidatorContext?): Boolean {
    this.requestUserAgent(any)?.let { return it.isNotBlank() }
    return false
  }

  private fun requestUserAgent(any: Any): String? =
    try {
      (any as HttpServletRequest).getHeader("User-Agent").ifBlank { null }
    } catch (illegalStateException: IllegalStateException) {
      null
    } catch (classCastException: ClassCastException) {
      null
    }
}
