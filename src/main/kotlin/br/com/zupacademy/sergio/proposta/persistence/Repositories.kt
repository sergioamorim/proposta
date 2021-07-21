package br.com.zupacademy.sergio.proposta.persistence

import br.com.zupacademy.sergio.proposta.model.Biometry
import br.com.zupacademy.sergio.proposta.model.CreditCard
import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalState
import org.springframework.data.jpa.repository.JpaRepository

interface ProposalRepository : JpaRepository<Proposal, String> {
  fun findByStateAndCreditCardNumberIsNull(state: ProposalState): Collection<Proposal>
}

interface CreditCardRepository : JpaRepository<CreditCard, String>

interface BiometryRepository : JpaRepository<Biometry, String>
