package br.com.zupacademy.sergio.proposta.scheduling

import br.com.zupacademy.sergio.proposta.feign.CreditCardClient
import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalState
import br.com.zupacademy.sergio.proposta.model.external.CreditCardRequest
import br.com.zupacademy.sergio.proposta.model.external.CreditCardResponse
import br.com.zupacademy.sergio.proposta.persistence.ProposalShortTransaction
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CreditCardLinker @Autowired constructor(
  private val creditCardClient: CreditCardClient,
  private val proposalShortTransaction: ProposalShortTransaction
) {

  @Scheduled(fixedDelayString = "\${schedule.fixed-delay.credit-cards-linker}")
  private fun linkCreditCards() {
    this.proposalShortTransaction.findByStateAndCreditCardNumberIsNull(
      ProposalState.ELEGIVEL
    ).forEach { proposal ->
      this.updateProposalWhenCreditCardResponseExists(
        proposal, this.creditCardResponse(proposal)
      )
    }
  }

  private fun creditCardResponse(proposal: Proposal): CreditCardResponse? =
    try {
      this.creditCardClient.creditCardResponse(CreditCardRequest(proposal))
    } catch (feignException: FeignException) {
      null
    }

  private fun updateProposalWhenCreditCardResponseExists(
    proposal: Proposal, creditCardResponse: CreditCardResponse?
  ) {
    if (null != creditCardResponse) {
      LoggerFactory.getLogger(javaClass).info(
        "Linked credit card to " +
        "${
          this.proposalShortTransaction.save(
            proposal.withCreditCardNumber(creditCardResponse.id)
          )
        }"
      )
    }
  }
}
