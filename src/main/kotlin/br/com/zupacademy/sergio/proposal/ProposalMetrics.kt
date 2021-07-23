package br.com.zupacademy.sergio.proposal

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProposalMetrics @Autowired constructor(
  private val meterRegistry: MeterRegistry
) {
  val proposalCreationsCounter: Counter = this.meterRegistry.counter(
    "proposal_creations"
  )

  val proposalReadingTimer: Timer = this.meterRegistry.timer("proposal_reading")
}
