package com.mihkels.graphite.client

import mu.KLogging
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.net.UnknownHostException

data class  GraphiteSettings(val hostUrl: String = "localhost", val hostPort: Int = 2003)

interface GraphiteClient {
    fun send(graphiteMetric: GraphiteMetric)
    fun send(graphiteMetrics : Collection<GraphiteMetric>)
}

class BasicGraphiteClient(private val graphiteSettings: GraphiteSettings): GraphiteClient {
    companion object: KLogging()

    override fun send(graphiteMetric: GraphiteMetric) {
        logger.info { "Start Sending out metric: $graphiteMetric" }
        sender { writer ->
            val message = convertToSting(graphiteMetric)
            logger.info { "Output message: $message" }
            writer.print(message)
        }
        logger.info { "Finished sending out metric" }
    }

    override fun send(graphiteMetrics: Collection<GraphiteMetric>) {
        logger.info { "Multi metric send started" }
        sender { writer ->
            graphiteMetrics.forEach {
                logger.info { "Sending out: $it" }
                writer.print(convertToSting(it))
            }
        }
        logger.info { "Finished sending out multi metric to graphite" }
    }

    private fun sender(callback: (PrintWriter) -> Unit) {
        try {
            val socket = Socket(graphiteSettings.hostUrl, graphiteSettings.hostPort)
            logger.info { "Connection status: ${socket.isConnected}" }
            val outputStream = socket.getOutputStream()
            val writer = PrintWriter(outputStream, true)
            callback(writer)

            writer.close()
            socket.close()
        } catch (e: UnknownHostException) {
            throw GraphiteException("Unknown host: ${graphiteSettings.hostUrl}")
        } catch (e: IOException) {
            throw GraphiteException("Error while writing data to graphite: ${e.message}")
        }
    }

    private fun convertToSting(metric: GraphiteMetric) =
            "${metric.metricName} ${metric.metricValue} ${metric.timestamp}\n"

}

class GraphiteException(message: String) : Exception(message)
