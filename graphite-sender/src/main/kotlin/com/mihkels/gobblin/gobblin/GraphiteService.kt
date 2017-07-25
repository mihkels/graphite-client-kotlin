package com.mihkels.gobblin.gobblin

import com.mihkels.graphite.client.GraphiteClient
import com.mihkels.graphite.client.GraphiteMetric
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class GraphiteService(private val graphiteClient: GraphiteClient) {
    companion object: KLogging()

    fun collectAndSendMetrics(metrics: List<GraphiteMetric> = emptyList()) {
        metrics.forEach {
            logger.info { it }
            graphiteClient.send(it)
        }
    }
}