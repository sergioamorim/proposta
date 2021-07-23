package br.com.zupacademy.sergio.proposal.persistence

import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.model.Proposal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ShortTransaction @Autowired constructor(
  private val proposalRepository: ProposalRepository,
  private val creditCardRepository: CreditCardRepository
) {
  @Transactional
  fun save(proposal: Proposal): Proposal =
    this.proposalRepository.save(proposal)

  @Transactional
  fun save(creditCard: CreditCard): CreditCard =
    this.creditCardRepository.save(creditCard)
}
