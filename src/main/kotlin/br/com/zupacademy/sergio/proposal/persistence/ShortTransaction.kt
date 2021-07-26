package br.com.zupacademy.sergio.proposal.persistence

import br.com.zupacademy.sergio.proposal.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ShortTransaction @Autowired constructor(
  private val proposalRepository: ProposalRepository,
  private val creditCardRepository: CreditCardRepository,
  private val blockRepository: BlockRepository,
  private val travelNoticeRepository: TravelNoticeRepository,
  private val walletRepository: WalletRepository
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

  @Transactional
  fun save(wallet: Wallet): Wallet = this.walletRepository.save(wallet)
}
