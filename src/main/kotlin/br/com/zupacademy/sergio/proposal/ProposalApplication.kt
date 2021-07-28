package br.com.zupacademy.sergio.proposal

import br.com.zupacademy.sergio.proposal.security.Encryption
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties(Encryption::class)
@EnableJpaRepositories(enableDefaultTransactions = false)
class ProposalApplicationKt {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ProposalApplicationKt>(*args)
		}
	}
}
