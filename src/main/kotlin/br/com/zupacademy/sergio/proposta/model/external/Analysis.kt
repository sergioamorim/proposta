package br.com.zupacademy.sergio.proposta.model.external

import br.com.zupacademy.sergio.proposta.model.Proposal
import br.com.zupacademy.sergio.proposta.model.ProposalState

class AnalysisRequest(proposal: Proposal) {
  val documento: String = proposal.nationalRegistryId
  val nome: String = proposal.name
  val idProposta: String? = proposal.id
}

class AnalysisResponse(
  private val documento: String,
  private val nome: String,
  private val resultadoSolicitacao: AnalysisResult,
  private val idProposta: String
) {
  fun proposalState(): ProposalState = this.resultadoSolicitacao.proposalState()

  enum class AnalysisResult {
    COM_RESTRICAO, SEM_RESTRICAO;

    fun proposalState(): ProposalState =
      if (this == SEM_RESTRICAO) ProposalState.ELEGIVEL
      else ProposalState.NAO_ELEGIVEL
  }
}
