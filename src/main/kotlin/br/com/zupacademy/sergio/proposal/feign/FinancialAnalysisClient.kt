package br.com.zupacademy.sergio.proposal.feign

import br.com.zupacademy.sergio.proposal.model.external.AnalysisRequest
import br.com.zupacademy.sergio.proposal.model.external.AnalysisResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
  name = "financial-analysis-api",
  url = "\${feign.client.config.financial-analysis-api.server-url}"
)
interface FinancialAnalysisClient {

  @PostMapping("\${feign.client.config.financial-analysis-api.endpoint}")
  fun analysisResponse(analysisRequest: AnalysisRequest): AnalysisResponse

  @GetMapping("\${feign.client.config.financial-analysis-api.health-endpoint}")
  fun health()

}
