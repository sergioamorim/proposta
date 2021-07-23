package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.feign.CreditCardClient
import br.com.zupacademy.sergio.proposal.model.Block
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.persistence.CreditCardRepository
import br.com.zupacademy.sergio.proposal.persistence.ShortTransaction
import br.com.zupacademy.sergio.proposal.validation.IdExists
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@Validated
@RestController
class BlockController @Autowired constructor(
  private val shortTransaction: ShortTransaction,
  private val creditCardRepository: CreditCardRepository,
  private val creditCardClient: CreditCardClient
) {

  @PostMapping("/credit-cards/{creditCardId}/block")
  fun createBlock(

    @PathVariable
    @IdExists(entityClass = CreditCard::class)
    creditCardId: String,

    httpServletRequest: HttpServletRequest
  ): ResponseEntity<Any> {

    this.requestUserAgent(httpServletRequest)?.let { requestUserAgent: String ->
      return okAfterPersistBlockWhenUniqueOrUnprocessableEntityWhenDuplicate(
        requestUserAgent = requestUserAgent,
        requestIp = httpServletRequest.remoteAddr,
        creditCard = this.creditCardRepository.getById(creditCardId)
      )
    }
    return ResponseEntity.badRequest().build()
  }

  private fun okAfterPersistBlockWhenUniqueOrUnprocessableEntityWhenDuplicate(
    requestUserAgent: String, requestIp: String, creditCard: CreditCard
  ): ResponseEntity<Any> {

    creditCard.block?.let { return ResponseEntity.unprocessableEntity().build() }

    if (this.blockedOnLegacySystem(creditCard)) {
      this.shortTransaction.save(creditCard.blocked())
    }

    LoggerFactory.getLogger(javaClass).info(
      "Created ${
        this.shortTransaction.save(
          Block(
            creditCard, requestIp, requestUserAgent
          )
        )
      }"
    )
    return ResponseEntity.ok().build()
  }

  private fun requestUserAgent(httpServletRequest: HttpServletRequest): String? =
    try {
      httpServletRequest.getHeader("User-Agent").ifBlank { null }
    } catch (illegalStateException: IllegalStateException) {
      null
    }

  private fun blockedOnLegacySystem(creditCard: CreditCard): Boolean =
    try {
      this.creditCardClient.blockCreditCard(creditCardNumber = creditCard.number)
      true
    } catch (feignException: FeignException) {
      false
    }

}
