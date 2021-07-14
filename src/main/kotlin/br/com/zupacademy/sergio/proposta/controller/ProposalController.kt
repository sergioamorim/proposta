package br.com.zupacademy.sergio.proposta.controller

import br.com.zupacademy.sergio.proposta.Proposal
import br.com.zupacademy.sergio.proposta.ProposalRepository
import br.com.zupacademy.sergio.proposta.ProposalRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
class ProposalController @Autowired constructor(
  val proposalRepository: ProposalRepository
) {

  val logger: Logger = LoggerFactory.getLogger(javaClass)

  @PostMapping("/proposals")
  fun createProposal(
    @RequestBody @Valid proposalRequest: ProposalRequest,
    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<String> {

    val proposal: Proposal = this.proposalRepository.save(
      proposalRequest.toProposal()
    )

    this.logger.info(
      "Created proposal with nationalRegistryId={} and salary={}",
      proposal.obfuscatedNationalRegistryId(),
      proposal.getSalary()
    )

    return ResponseEntity
      .created(
        uriComponentsBuilder
          .path("/proposals/{proposalId}")
          .buildAndExpand(
            proposal.getId()
          )
          .toUri()
      )
      .build()
  }
}
