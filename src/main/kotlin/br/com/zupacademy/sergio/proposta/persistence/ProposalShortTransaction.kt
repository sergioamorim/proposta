package br.com.zupacademy.sergio.proposta.persistence

import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
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

  @Transactional
  fun findById(proposalId: String): Optional<Proposal> =
    this.proposalRepository.findById(proposalId)
}
