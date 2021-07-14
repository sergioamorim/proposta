import org.hibernate.validator.constraints.CompositionType.OR
import org.hibernate.validator.constraints.ConstraintComposition
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@CPF
@ConstraintComposition(OR)
@CNPJ
@ReportAsSingleViolation
@MustBeDocumented
@Constraint(validatedBy = [])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CpfOrCnpj(
  val message: String = "must be a valid CPF or CNPJ",
  val groups: Array<KClass<Any>> = [],
  val payload: Array<KClass<Payload>> = []
)
