package br.com.zupacademy.sergio.proposta.validation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllersExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(
    methodArgumentNotValidException: MethodArgumentNotValidException
  ): ResponseEntity<Any> {

    if ("UniqueValue" == methodArgumentNotValidException.fieldError?.code)
      return ResponseEntity.unprocessableEntity().build()

    return ResponseEntity.badRequest().build()
  }

}
