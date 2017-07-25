package com.mihkels.gobblin.gobblin

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GraphiteLoggingApplication(
        private val graphiteService: GraphiteService,
        private val executeService: BashExecuteService
) : CommandLineRunner {
    override fun run(vararg p0: String?) {
        val scriptResults = executeService.executeService()
        graphiteService.collectAndSendMetrics(scriptResults)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(GraphiteLoggingApplication::class.java, *args)
}
