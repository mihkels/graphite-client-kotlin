package com.mihkels.gobblin.gobblin

import com.mihkels.graphite.client.GraphiteClient
import com.mihkels.graphite.client.GraphiteMetric
import mu.KLogging
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GraphiteService(private val graphiteClient: GraphiteClient) {
    companion object: KLogging()

    fun collectAndSendMetrics() {
        val epoch = Instant.now().toEpochMilli()
        val metric = GraphiteMetric("demo.metric", "hello world", epoch)

        logger.info { metric }
        graphiteClient.send(metric)
    }
}