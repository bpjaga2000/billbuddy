package dev.bpj4.billbuddy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class BillbuddyApplication

fun main(args: Array<String>) {
	runApplication<BillbuddyApplication>(*args)
}
