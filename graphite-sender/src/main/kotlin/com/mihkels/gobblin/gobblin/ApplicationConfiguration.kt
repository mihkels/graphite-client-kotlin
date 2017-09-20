package com.mihkels.gobblin.gobblin

import com.mihkels.graphite.bash.BashExecutor
import com.mihkels.graphite.bash.SimpleBashExecutor
import com.mihkels.graphite.client.BasicGraphiteClient
import com.mihkels.graphite.client.GraphiteClient
import com.mihkels.graphite.client.GraphiteClientProperties
import com.mihkels.graphite.client.SocketSender
import mu.KLogging
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
    fun graphiteSettings(): GraphiteClientProperties =
            GraphiteClientProperties(properties.serverUrl, properties.serverPort)

    @Bean
    fun graphiteClient(graphiteSettings: GraphiteClientProperties): GraphiteClient =
            BasicGraphiteClient(graphiteSettings, SocketSender())
}

@ConfigurationProperties(prefix = "graphite.script.executor")
data class ScriptExecutorSettings(val scriptDirectory: String = "scripts")

@Configuration
@EnableConfigurationProperties(ScriptExecutorSettings::class)
class ScriptExecutorConfiguration {
    @Bean
    fun bashExecutor(): BashExecutor = SimpleBashExecutor()
}
