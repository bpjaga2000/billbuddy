package dev.bpj4.billbuddy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BillbuddyApplication

fun main(args: Array<String>) {
	runApplication<BillbuddyApplication>(*args)
}
