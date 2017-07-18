package com.mihkels.gobblin.gobblin

import mu.KLogging
import net.savantly.graphite.GraphiteClient
import net.savantly.graphite.impl.SimpleCarbonMetric
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GraphiteService(private val graphiteClient: GraphiteClient) {
    companion object: KLogging()

    fun collectAndSendMetrics() {
        val epoch = Instant.now().toEpochMilli()
        val metric = SimpleCarbonMetric("demo.metric", "hello world", epoch)

        logger.info { metric }
        graphiteClient.saveCarbonMetrics(metric)
    }
}