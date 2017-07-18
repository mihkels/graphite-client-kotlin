package com.mihkels.gobblin.gobblin

import mu.KLogging
import net.savantly.graphite.GraphiteClient
import net.savantly.graphite.GraphiteClientFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.SocketException

@ConfigurationProperties
data class GraphiteProperties(val serverUrl: String = "localhost", val serverPort: Int = 2003)

@Configuration
@EnableConfigurationProperties(GraphiteProperties::class)
class GraphiteClientConfiguration(private val properties: GraphiteProperties) {
    companion object: KLogging()

    @Bean
    fun graphiteClient(): GraphiteClient {
        try {
            logger.info { "Created Graphite client with host: ${properties.serverUrl} and port: ${properties.serverPort}" }
            return GraphiteClientFactory.defaultGraphiteClient(properties.serverUrl)
        } catch (ex: SocketException) {
            logger.warn { "Failed to connect to ${properties.serverUrl} with port ${properties.serverPort}" }
            throw ex
        }
    }
}

@SpringBootApplication
class GraphiteLoggingApplication(private val graphiteService: GraphiteService) : CommandLineRunner {
    override fun run(vararg p0: String?) {
        graphiteService.collectAndSendMetrics()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(GraphiteLoggingApplication::class.java, *args)
}
