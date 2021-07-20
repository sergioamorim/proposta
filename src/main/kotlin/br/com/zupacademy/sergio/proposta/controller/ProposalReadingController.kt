package br.com.zupacademy.sergio.proposta.controller

import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalDetail
import br.com.zupacademy.sergio.proposta.persistence.ProposalShortTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ProposalReadingController @Autowired constructor(
  private val proposalShortTransaction: ProposalShortTransaction
) {

  @GetMapping("/proposals/{proposalId}")
  fun detailProposal(
    @PathVariable proposalId: String
  ): ResponseEntity<ProposalDetail> =
    this.proposalDetailOrNotFound(
      this.proposalShortTransaction.findById(proposalId)
    )

  private fun proposalDetailOrNotFound(
    optionalProposal: Optional<Proposal>
  ): ResponseEntity<ProposalDetail> =
    if (optionalProposal.isPresent) {
      ResponseEntity.ok(ProposalDetail(optionalProposal.get()))
    } else {
      ResponseEntity.notFound().build()
    }
}
