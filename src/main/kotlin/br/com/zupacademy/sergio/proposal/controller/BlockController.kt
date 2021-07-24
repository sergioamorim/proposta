package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.feign.CreditCardClient
import br.com.zupacademy.sergio.proposal.model.Block
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.persistence.CreditCardRepository
import br.com.zupacademy.sergio.proposal.persistence.ShortTransaction
import br.com.zupacademy.sergio.proposal.validation.IdExists
import br.com.zupacademy.sergio.proposal.validation.UniqueValue
import br.com.zupacademy.sergio.proposal.validation.UserAgentNotBlank
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
    @UniqueValue(domainClass = Block::class, fieldName = "creditCard.id")
    creditCardId: String,

    @UserAgentNotBlank
    httpServletRequest: HttpServletRequest
  ) {
    this.persistAndLogBlock(
      requestUserAgent = httpServletRequest.getHeader("User-Agent"),
      requestIp = httpServletRequest.remoteAddr,
      creditCard = this.creditCardRepository.getById(creditCardId)
    )
  }

  private fun persistAndLogBlock(
    requestUserAgent: String, requestIp: String, creditCard: CreditCard
  ) {
    this.notifyBlockToLegacySystem(creditCard)

    LoggerFactory.getLogger(javaClass).info(
      "Created ${
        this.shortTransaction.save(Block(creditCard, requestIp, requestUserAgent))
      }"
    )
  }

  private fun notifyBlockToLegacySystem(creditCard: CreditCard) {
    try {
      this.creditCardClient.blockCreditCard(creditCardNumber = creditCard.number)
      this.shortTransaction.save(creditCard.blocked())  // save when no exception is thrown
    } catch (feignException: FeignException) {
    }  // exception expected, do nothing
  }

}
