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
    if ("UniqueValue" == violationAnnotation(methodArgumentNotValidException)) {
      ResponseEntity.unprocessableEntity().build()
    } else {
      ResponseEntity.badRequest().build()
    }

  @ExceptionHandler(ConstraintViolationException::class)
  fun handleConstraintViolationException(
    constraintViolationException: ConstraintViolationException
  ): ResponseEntity<Any> =
    when (violationAnnotation(constraintViolationException)) {
      "IdExists" -> ResponseEntity.notFound().build()
      "UniqueValue" -> ResponseEntity.unprocessableEntity().build()
      else -> ResponseEntity.badRequest().build()
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

private fun violationAnnotation(
  methodArgumentNotValidException: MethodArgumentNotValidException
): String? =
  if (methodArgumentNotValidException.bindingResult.allErrors.iterator().hasNext()) {
    methodArgumentNotValidException
      .bindingResult.allErrors.iterator().next()
      .code
  } else {
    null
  }
