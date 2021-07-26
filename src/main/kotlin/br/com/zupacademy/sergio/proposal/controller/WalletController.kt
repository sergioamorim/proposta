package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.feign.CreditCardClient
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.model.Wallet
import br.com.zupacademy.sergio.proposal.model.WalletRequest
import br.com.zupacademy.sergio.proposal.model.external.WalletNotificationRequest
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
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@Validated
@RestController
class WalletController @Autowired constructor(
  private val shortTransaction: ShortTransaction,
  private val creditCardRepository: CreditCardRepository,
  private val creditCardClient: CreditCardClient
) {

  @PostMapping("/credit-cards/{creditCardId}/wallets")
  fun createWallet(

    @PathVariable
    @IdExists(entityClass = CreditCard::class)
    creditCardId: String,

    @Valid
    @RequestBody
    walletRequest: WalletRequest,

    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<Any> =
    this.createdWhenUniqueAndBankingSystemAccepts(
      creditCard = this.creditCardRepository.getById(creditCardId),
      walletRequest = walletRequest,
      uriComponentsBuilder = uriComponentsBuilder
    )

  private fun createdWhenUniqueAndBankingSystemAccepts(
    creditCard: CreditCard,
    walletRequest: WalletRequest,
    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<Any> =
    if (creditCard.hasWalletOfType(walletRequest.type)) {
      ResponseEntity.unprocessableEntity().build()
    } else {
      this.createdWhenBankingSystemAccepts(
        wallet = walletRequest.toWallet(creditCard),
        uriComponentsBuilder = uriComponentsBuilder
      )
    }

  private fun createdWhenBankingSystemAccepts(
    wallet: Wallet, uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<Any> =
    try {
      this.creditCardClient.notifyWallet(
        creditCardNumber = wallet.getCreditCardNumber(),
        walletNotificationRequest = WalletNotificationRequest(wallet)
      )
      this.createdStatusOfSavedWallet(  // returns created only when no exception is thrown
        wallet = this.shortTransaction.save(wallet),
        uriComponentsBuilder = uriComponentsBuilder
      )
    } catch (feignException: FeignException) {
      ResponseEntity.ok().build()  // nothing to do when the banking system returns 4xx or 5xx
    }

  private fun createdStatusOfSavedWallet(
    wallet: Wallet, uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<Any> =
    ResponseEntity.created(
      uriComponentsBuilder
        .path("/credit-cards/${wallet.getCreditCardId()}/wallets/{walletId}")
        .buildAndExpand(this.loggedWalletId(wallet))
        .toUri()
    ).build()

  private fun loggedWalletId(wallet: Wallet): String? {
    LoggerFactory.getLogger(javaClass).info("Created $wallet")
    return wallet.id
  }
}
