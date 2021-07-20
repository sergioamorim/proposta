package br.com.zupacademy.sergio.proposta.controller

import br.com.zupacademy.sergio.proposta.feign.FinancialAnalysisClient
import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalRequest
import br.com.zupacademy.sergio.proposta.model.external.AnalysisRequest
import br.com.zupacademy.sergio.proposta.model.external.AnalysisResponse
import br.com.zupacademy.sergio.proposta.persistence.ProposalShortTransaction
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
  ): ResponseEntity<String> {

    val proposal: Proposal = this.proposalShortTransaction.save(
      proposalRequest.toProposal()
    )

    this.updateProposalWhenAnalysisResponseExists(
      proposal, this.analysisResponseFromProposal(proposal)
    )

    this.logger.info("Created $proposal")
    return ResponseEntity
      .created(
        uriComponentsBuilder
          .path("/proposals/{proposalId}")
          .buildAndExpand(proposal.id)
          .toUri()
      )
      .build()
  }

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

  private fun updateProposalWhenAnalysisResponseExists(
    proposal: Proposal, analysisResponse: AnalysisResponse?
  ) {
    if (null != analysisResponse) {
      this.proposalShortTransaction.save(
        proposal.withState(analysisResponse.proposalState())
      )
    }
  }
}
