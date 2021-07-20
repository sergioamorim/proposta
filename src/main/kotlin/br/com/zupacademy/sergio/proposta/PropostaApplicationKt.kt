package br.com.zupacademy.sergio.proposta

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
class PropostaApplicationKt {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<PropostaApplicationKt>(*args)
		}
	}
}
