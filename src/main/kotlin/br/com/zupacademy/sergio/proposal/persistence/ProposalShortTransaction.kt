package br.com.zupacademy.sergio.proposal.persistence

import br.com.zupacademy.sergio.proposal.model.Proposal
import br.com.zupacademy.sergio.proposal.model.ProposalState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ProposalShortTransaction @Autowired constructor(
  private val proposalRepository: ProposalRepository
) {

  @Transactional
  fun save(proposal: Proposal): Proposal =
    this.proposalRepository.save(proposal)

  @Transactional
  fun findByStateAndCreditCardNumberIsNull(state: ProposalState): Collection<Proposal> =
    this.proposalRepository.findByStateAndCreditCardNumberIsNull(state)

}
