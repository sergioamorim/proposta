package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.feign.CreditCardClient
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.model.TravelNotice
import br.com.zupacademy.sergio.proposal.model.TravelNoticeRequest
import br.com.zupacademy.sergio.proposal.model.external.TravelNoticeNotificationRequest
import br.com.zupacademy.sergio.proposal.persistence.CreditCardRepository
import br.com.zupacademy.sergio.proposal.persistence.ShortTransaction
import br.com.zupacademy.sergio.proposal.validation.IdExists
import br.com.zupacademy.sergio.proposal.validation.UserAgentNotBlank
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Validated
@RestController
class TravelNoticeController @Autowired constructor(
  private val shortTransaction: ShortTransaction,
  private val creditCardRepository: CreditCardRepository,
  private val creditCardClient: CreditCardClient
) {

  @PostMapping("/credit-cards/{creditCardId}/travel-notices")
  fun createTravelNotice(

    @PathVariable
    @IdExists(entityClass = CreditCard::class)
    creditCardId: String,

    @Valid
    @RequestBody
    travelNoticeRequest: TravelNoticeRequest,

    @UserAgentNotBlank
    httpServletRequest: HttpServletRequest
  ) {

    this.notifyToBankingSystemThenSaveTravelNotice(
      travelNoticeRequest.toTravelNotice(
        creditCard = this.creditCardRepository.findById(creditCardId)
          .orElseThrow(),  // IdExists shall guarantee this doesn't throw
        requestUserAgent = httpServletRequest.getHeader("User-Agent"),
        requestIp = httpServletRequest.remoteAddr
      )
    )
  }

  private fun notifyToBankingSystemThenSaveTravelNotice(travelNotice: TravelNotice) {
    try {
      this.creditCardClient.notifyTravelNotice(
        creditCardNumber = travelNotice.getCreditCardNumber(),
        travelNoticeNotificationRequest = TravelNoticeNotificationRequest(travelNotice)
      )
      this.saveAndLogTravelNotice(travelNotice)  // save only when no exception is thrown
    } catch (feignException: FeignException) {
    }  // expected, purposefully do nothing
  }

  private fun saveAndLogTravelNotice(travelNotice: TravelNotice) {
    LoggerFactory.getLogger(javaClass).info(
      "Created ${this.shortTransaction.save(travelNotice)}"
    )
  }

}
