package com.mihkels.gobblin.gobblin

import com.mihkels.graphite.client.BasicGraphiteClient
import com.mihkels.graphite.client.GraphiteClient
import com.mihkels.graphite.client.GraphiteSettings
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "graphite.sender`")
data class GraphiteProperties(val serverUrl: String = "localhost", val serverPort: Int = 2003)

@Configuration
@EnableConfigurationProperties(GraphiteProperties::class)
class GraphiteClientConfiguration(private val properties: GraphiteProperties) {
    companion object: KLogging()

    @Bean
    fun graphiteSettings(): GraphiteSettings = GraphiteSettings(properties.serverUrl, properties.serverPort)

    @Bean
    fun graphiteClient(graphiteSettings: GraphiteSettings): GraphiteClient = BasicGraphiteClient(graphiteSettings)
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
