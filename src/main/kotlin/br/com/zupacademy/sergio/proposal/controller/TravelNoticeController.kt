package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.model.TravelNoticeRequest
import br.com.zupacademy.sergio.proposal.persistence.CreditCardRepository
import br.com.zupacademy.sergio.proposal.persistence.TravelNoticeRepository
import br.com.zupacademy.sergio.proposal.validation.IdExists
import br.com.zupacademy.sergio.proposal.validation.UserAgentNotBlank
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@RestController
class TravelNoticeController @Autowired constructor(
  private val travelNoticeRepository: TravelNoticeRepository,
  private val creditCardRepository: CreditCardRepository
) {

  @Transactional
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

    LoggerFactory.getLogger(javaClass).info(
      "Created ${
        this.travelNoticeRepository.save(
          travelNoticeRequest.toTravelNotice(
            creditCard = this.creditCardRepository.getById(creditCardId),
            requestUserAgent = httpServletRequest.getHeader("User-Agent"),
            requestIp = httpServletRequest.remoteAddr
          )
        )
      }"
    )

  }

}
