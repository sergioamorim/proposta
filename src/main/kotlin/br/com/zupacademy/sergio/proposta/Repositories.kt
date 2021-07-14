package br.com.zupacademy.sergio.proposta

import org.springframework.data.repository.Repository

@org.springframework.stereotype.Repository
interface ProposalRepository : Repository<Proposal, Long> {
  fun save(proposal: Proposal): Proposal
}
