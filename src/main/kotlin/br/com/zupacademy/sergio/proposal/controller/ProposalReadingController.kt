package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.ProposalMetrics
import br.com.zupacademy.sergio.proposal.model.Proposal
import br.com.zupacademy.sergio.proposal.model.ProposalDetail
import br.com.zupacademy.sergio.proposal.persistence.ProposalRepository
import br.com.zupacademy.sergio.proposal.validation.IdExists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class ProposalReadingController @Autowired constructor(
  private val proposalRepository: ProposalRepository,
  private val proposalMetrics: ProposalMetrics
) {

  private lateinit var responseEntity: ResponseEntity<ProposalDetail>

  @GetMapping("/proposals/{proposalId}")
  fun readProposal(
    @PathVariable @IdExists(entityClass = Proposal::class) proposalId: String
  ): ResponseEntity<ProposalDetail> {

    proposalMetrics.proposalReadingTimer.record {
      this.responseEntity = ResponseEntity.ok(
        ProposalDetail(
          this.proposalRepository.findById(proposalId).orElseThrow()
        )  // IdExists shall guarantee this doesn't throw
      )
    }

    return this.responseEntity
  }
}
