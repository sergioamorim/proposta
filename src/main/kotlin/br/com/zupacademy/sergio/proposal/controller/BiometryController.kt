package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.model.Biometry
import br.com.zupacademy.sergio.proposal.model.BiometryRequest
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.persistence.BiometryRepository
import br.com.zupacademy.sergio.proposal.persistence.CreditCardRepository
import br.com.zupacademy.sergio.proposal.validation.IdExists
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@RestController
class BiometryController @Autowired constructor(
  private val creditCardRepository: CreditCardRepository,
  private val biometryRepository: BiometryRepository
) {

  @Transactional
  @PostMapping("/credit-cards/{creditCardId}/biometrics")
  fun createBiometry(
    @PathVariable @IdExists(entityClass = CreditCard::class) creditCardId: String,
    @RequestBody @Valid biometryRequest: BiometryRequest,
    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<Any> =
    ResponseEntity.created(
      uriComponentsBuilder
        .path("/credit-cards/$creditCardId/biometrics/{biometryId}")
        .buildAndExpand(
          loggedBiometryId(
            this.biometryRepository.save(
              biometryRequest.toBiometry(
                this.creditCardRepository.findById(creditCardId)
                  .orElseThrow()  // IdExists shall guarantee this doesn't throw
              )
            )
          )
        )
        .toUri()
    ).build()

  private fun loggedBiometryId(biometry: Biometry): String? {
    LoggerFactory.getLogger(javaClass).info("Created $biometry")
    return biometry.id
  }
}
