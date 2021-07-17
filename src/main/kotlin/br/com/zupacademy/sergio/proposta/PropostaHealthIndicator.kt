package br.com.zupacademy.sergio.proposta

import br.com.zupacademy.sergio.proposta.feign.FinancialAnalysis
import feign.FeignException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class PropostaHealthIndicator @Autowired constructor(
  private val financialAnalysis: FinancialAnalysis
) : HealthIndicator {
  override fun health(): Health =
    try {
      this.financialAnalysis.health()
      Health.up().withDetail("financial-analysis-api", "UP").build()
    } catch (feignException: FeignException) {
      Health.down().withDetail("financial-analysis-api", "DOWN").build()
    }
}
