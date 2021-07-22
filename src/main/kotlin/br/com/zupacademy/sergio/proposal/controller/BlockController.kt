package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.model.Block
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.persistence.BlockRepository
import br.com.zupacademy.sergio.proposal.persistence.CreditCardRepository
import br.com.zupacademy.sergio.proposal.validation.IdExists
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Validated
@RestController
class BlockController @Autowired constructor(
  private val blockRepository: BlockRepository,
  private val creditCardRepository: CreditCardRepository
) {

  @Transactional
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

    LoggerFactory.getLogger(javaClass).info(
      "Created ${
        this.blockRepository.save(Block(creditCard, requestIp, requestUserAgent))
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

}
