package br.com.zupacademy.sergio.proposal.persistence

import br.com.zupacademy.sergio.proposal.model.Block
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.model.Proposal
import br.com.zupacademy.sergio.proposal.model.TravelNotice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ShortTransaction @Autowired constructor(
  private val proposalRepository: ProposalRepository,
  private val creditCardRepository: CreditCardRepository,
  private val blockRepository: BlockRepository,
  private val travelNoticeRepository: TravelNoticeRepository
) {
  @Transactional
  fun save(proposal: Proposal): Proposal =
    this.proposalRepository.save(proposal)

  @Transactional
  fun save(creditCard: CreditCard): CreditCard =
    this.creditCardRepository.save(creditCard)

  @Transactional
  fun save(block: Block): Block = this.blockRepository.save(block)

  @Transactional
  fun save(travelNotice: TravelNotice): TravelNotice =
    this.travelNoticeRepository.save(travelNotice)
}
