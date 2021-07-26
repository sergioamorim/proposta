package br.com.zupacademy.sergio.proposal.persistence

import br.com.zupacademy.sergio.proposal.model.*
import org.springframework.data.jpa.repository.JpaRepository

interface ProposalRepository : JpaRepository<Proposal, String> {
  fun findByStateAndCreditCardNumberIsNull(state: ProposalState): Collection<Proposal>
}

interface CreditCardRepository : JpaRepository<CreditCard, String>

interface BiometryRepository : JpaRepository<Biometry, String>

interface BlockRepository : JpaRepository<Block, String>

interface TravelNoticeRepository : JpaRepository<TravelNotice, String>

interface WalletRepository : JpaRepository<Wallet, String>
