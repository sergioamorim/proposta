package br.com.zupacademy.sergio.proposta

import br.com.zupacademy.sergio.proposta.feign.FinancialAnalysisClient
import feign.FeignException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class PropostaHealthIndicator @Autowired constructor(
  private val financialAnalysisClient: FinancialAnalysisClient
) : HealthIndicator {
  override fun health(): Health = this.buildHealth(healthBuilder = Health.up())

  private fun buildHealth(healthBuilder: Health.Builder): Health =
    this.addFinancialAnalysisApiHealthTo(
      healthBuilder = this.addCreditCardApiHealthTo(healthBuilder)
    ).build()

  private fun addCreditCardApiHealthTo(
    healthBuilder: Health.Builder
  ): Health.Builder =
    try {
      this.financialAnalysisClient.health()
      healthBuilder.withDetail("credit-card-api", "UP")
    } catch (feignException: FeignException) {
      healthBuilder.withDetail("credit-card-api", "DOWN").down()
    }

  private fun addFinancialAnalysisApiHealthTo(
    healthBuilder: Health.Builder
  ): Health.Builder =
    try {
      this.financialAnalysisClient.health()
      healthBuilder.withDetail("financial-analysis-api", "UP")
    } catch (feignException: FeignException) {
      healthBuilder.withDetail("financial-analysis-api", "DOWN").down()
    }
}
