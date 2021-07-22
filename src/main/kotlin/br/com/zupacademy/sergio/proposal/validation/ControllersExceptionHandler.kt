package br.com.zupacademy.sergio.proposal.validation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ControllersExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(
    methodArgumentNotValidException: MethodArgumentNotValidException
  ): ResponseEntity<Any> =
    if ("UniqueValue" == methodArgumentNotValidException.fieldError?.code) {
      ResponseEntity.unprocessableEntity().build()
    } else {
      ResponseEntity.badRequest().build()
    }

  @ExceptionHandler(ConstraintViolationException::class)
  fun handleConstraintViolationException(
    constraintViolationException: ConstraintViolationException
  ): ResponseEntity<Any> =
    if ("IdExists" == violationAnnotation(constraintViolationException)) {
      ResponseEntity.notFound().build()
    } else {
      ResponseEntity.badRequest().build()
    }

  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgumentException(
    illegalArgumentException: IllegalArgumentException
  ): ResponseEntity<Any> = ResponseEntity.badRequest().build()
}

private fun violationAnnotation(
  constraintViolationException: ConstraintViolationException
): String? =
  if (constraintViolationException.constraintViolations.iterator().hasNext()) {
    constraintViolationException
      .constraintViolations.iterator().next()
      .constraintDescriptor.annotation.annotationClass.simpleName
  } else {
    null
  }
