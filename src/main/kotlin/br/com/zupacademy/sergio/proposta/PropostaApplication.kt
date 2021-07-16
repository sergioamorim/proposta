package br.com.zupacademy.sergio.proposta

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
class PropostaApplication

fun main(args: Array<String>) {
	runApplication<PropostaApplication>(*args)
}
