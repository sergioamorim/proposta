package br.com.zupacademy.sergio.proposal.persistence

import br.com.zupacademy.sergio.proposal.model.Biometry
import br.com.zupacademy.sergio.proposal.model.CreditCard
import br.com.zupacademy.sergio.proposal.model.Proposal
import br.com.zupacademy.sergio.proposal.model.ProposalState
import org.springframework.data.jpa.repository.JpaRepository

interface ProposalRepository : JpaRepository<Proposal, String> {
  fun findByStateAndCreditCardNumberIsNull(state: ProposalState): Collection<Proposal>
}

interface CreditCardRepository : JpaRepository<CreditCard, String>

interface BiometryRepository : JpaRepository<Biometry, String>
