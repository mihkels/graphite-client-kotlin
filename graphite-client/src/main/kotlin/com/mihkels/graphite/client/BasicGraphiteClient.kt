package com.mihkels.graphite.client

import mu.KotlinLogging
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.net.UnknownHostException
import java.time.Instant

private val logger = KotlinLogging.logger {  }

data class GraphiteClientProperties(
        var hostname: String = "localhost",
        var port: Int = 2003,
        var metricPrefix: String = ""
)

data class GraphiteMetric(
        val metricName: String,
        val metricValue: String,
        val timestamp: Long = Instant.now().toEpochMilli()
)

interface GraphiteClient {
    fun send(graphiteMetric: GraphiteMetric)
    fun send(graphiteMetrics: Collection<GraphiteMetric>)
}

interface DataSender {
    fun sendData(host: String = "", port: Int = 0, callback: (PrintWriter) -> Unit = {})
}

class SocketSender : DataSender {
    override fun sendData(host: String, port: Int, callback: (PrintWriter) -> Unit) {
        Socket(host, port).getOutputStream().use {
            PrintWriter(it, true).use { callback(it) }
        }
    }
}

class BasicGraphiteClient(
        private val properties: GraphiteClientProperties,
        private val dataSender: DataSender = SocketSender()
): GraphiteClient {
    override fun send(graphiteMetric: GraphiteMetric) {
        sender { writer ->
            val message = convertToSting(graphiteMetric)
            logger.debug { "Sending out message: $message" }
            writer.print(message)
        }
    }

    override fun send(graphiteMetrics: Collection<GraphiteMetric>) {
        logger.debug { "Start sending to graphite host: ${properties.hostname}:${properties.port}" }
        logger.info { "start sending out Graphtite metrics with count: ${graphiteMetrics.size}" }
        if (graphiteMetrics.isNotEmpty()) {
            sender { writer -> graphiteMetrics
                .map {
                    val modified = convertToSting(it)
                    logger.debug { "Sending out metric: $modified" }
                    modified
                }
                .forEach { writer.print(it) }
            }
        }
    }

    private fun sender(callback: (PrintWriter) -> Unit) {
        try {
            dataSender.sendData(properties.hostname, properties.port, callback)
        } catch (e: UnknownHostException) {
            throw GraphiteException("Unknown host: ${properties.hostname}")
        } catch (e: IOException) {
            throw GraphiteException("Error while writing data to graphite: ${e.message}")
        }
    }

    private fun addPrefix(metricName: String): String =
           if (properties.metricPrefix.isNotEmpty()) "${properties.metricPrefix}.$metricName" else metricName

    private fun convertToSting(metric: GraphiteMetric) =
            "${addPrefix(metric.metricName)} ${metric.metricValue} ${metric.timestamp / 1000}\n"
}

class GraphiteException(message: String) : Exception(message)
