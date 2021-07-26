package br.com.zupacademy.sergio.proposal.feign

import br.com.zupacademy.sergio.proposal.model.external.CreditCardRequest
import br.com.zupacademy.sergio.proposal.model.external.CreditCardResponse
import br.com.zupacademy.sergio.proposal.model.external.TravelNoticeNotificationRequest
import br.com.zupacademy.sergio.proposal.model.external.WalletNotificationRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Validated
@FeignClient(
  name = "credit-card-api",
  url = "\${feign.client.config.credit-card-api.server-url}"
)
interface CreditCardClient {

  @Valid
  @PostMapping("\${feign.client.config.credit-card-api.request-endpoint}")
  fun creditCardResponse(
    creditCardResponse: CreditCardRequest
  ): CreditCardResponse

  @PostMapping("\${feign.client.config.credit-card-api.block-endpoint}")
  fun blockCreditCard(
    @PathVariable creditCardNumber: String,
    claimingSystem: Map<String, Any> = mapOf("sistemaResponsavel" to "proposal")
  )

  @PostMapping("\${feign.client.config.credit-card-api.travel-notice-endpoint}")
  fun notifyTravelNotice(
    @PathVariable creditCardNumber: String,
    travelNoticeNotificationRequest: TravelNoticeNotificationRequest
  )

  @PostMapping("\${feign.client.config.credit-card-api.wallet-endpoint}")
  fun notifyWallet(
    @PathVariable creditCardNumber: String,
    walletNotificationRequest: WalletNotificationRequest
  )

  @GetMapping("\${feign.client.config.credit-card-api.health-endpoint}")
  fun health()
}
