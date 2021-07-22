package br.com.zupacademy.sergio.proposal.controller

import br.com.zupacademy.sergio.proposal.feign.FinancialAnalysisClient
import br.com.zupacademy.sergio.proposal.model.Proposal
import br.com.zupacademy.sergio.proposal.model.ProposalRequest
import br.com.zupacademy.sergio.proposal.model.external.AnalysisRequest
import br.com.zupacademy.sergio.proposal.model.external.AnalysisResponse
import br.com.zupacademy.sergio.proposal.persistence.ProposalShortTransaction
import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
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
class ProposalCreationController @Autowired constructor(
  private val proposalShortTransaction: ProposalShortTransaction,
  private val financialAnalysisClient: FinancialAnalysisClient,
  private val objectMapper: ObjectMapper
) {

  val logger: Logger = LoggerFactory.getLogger(javaClass)

  @PostMapping("/proposals")
  fun createProposal(
    @RequestBody @Valid proposalRequest: ProposalRequest,
    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<String> =
    ResponseEntity
      .created(
        uriComponentsBuilder
          .path("/proposals/{proposalId}")
          .buildAndExpand(
            this.loggedProposalId(
              this.analysedProposal(
                this.proposalShortTransaction.save(proposalRequest.toProposal())
              )
            )
          )
          .toUri()
      )
      .build()

  private fun loggedProposalId(proposal: Proposal): String? {
    this.logger.info("Created $proposal")
    return proposal.id
  }

  private fun analysedProposal(proposal: Proposal): Proposal =
    this.updatedProposalWhenAnalysisResponseExists(
      proposal, this.analysisResponseFromProposal(proposal)
    )

  private fun analysisResponseFromProposal(
    proposal: Proposal
  ): AnalysisResponse? =
    try {
      this.financialAnalysisClient.analysisResponse(AnalysisRequest(proposal))
    } catch (unprocessableEntity: FeignException.UnprocessableEntity) {  // expected
      this.objectMapper.readValue(
        unprocessableEntity.contentUTF8(), AnalysisResponse::class.java
      )
    } catch (feignException: FeignException) {  // unexpected
      logger.warn("Unable to get financial analysis for $proposal")
      null
    }

  private fun updatedProposalWhenAnalysisResponseExists(
    proposal: Proposal, analysisResponse: AnalysisResponse?
  ): Proposal =
    if (null != analysisResponse) {
      this.proposalShortTransaction.save(
        proposal.withState(analysisResponse.proposalState())
      )
    } else {
      proposal
    }
}
