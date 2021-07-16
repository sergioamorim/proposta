package br.com.zupacademy.sergio.proposta.feign

import br.com.zupacademy.sergio.proposta.model.external.AnalysisRequest
import br.com.zupacademy.sergio.proposta.model.external.AnalysisResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name = "financial-analysis", url = "\${feign.client.config.financial-analysis.url}")
interface FinancialAnalysis {

  @RequestMapping(method = [RequestMethod.POST])
  fun analysisResponse(analysisRequest: AnalysisRequest): AnalysisResponse

}
