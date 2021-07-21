package br.com.zupacademy.sergio.proposta.controller

import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalDetail
import br.com.zupacademy.sergio.proposta.persistence.ProposalRepository
import br.com.zupacademy.sergio.proposta.validation.IdExists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class ProposalReadingController @Autowired constructor(
  private val proposalRepository: ProposalRepository
) {

  @GetMapping("/proposals/{proposalId}")
  fun readProposal(
    @PathVariable @IdExists(entityClass = Proposal::class) proposalId: String
  ): ResponseEntity<ProposalDetail> =
    ResponseEntity.ok(
      ProposalDetail(this.proposalRepository.getById(proposalId))
    )
}
