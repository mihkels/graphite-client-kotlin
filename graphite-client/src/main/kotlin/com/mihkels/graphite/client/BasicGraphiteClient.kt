package com.mihkels.graphite.client

import mu.KLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

data class  GraphiteSettings(val hostUrl: String = "localhost", val hostPort: Int = 2003)

interface GraphiteClient {
    fun send(graphiteMetric: GraphiteMetric)
    fun send(graphiteMetrics : Collection<GraphiteMetric>)
}

class BasicGraphiteClient(private val graphiteSettings: GraphiteSettings): GraphiteClient {
    companion object: KLogging()

    override fun send(graphiteMetric: GraphiteMetric) {
        logger.info { "Start Sending out metric: $graphiteMetric" }
        sender { socket ->
            socket.send(prepareMetric(graphiteMetric))
        }
        logger.info { "Finished sending out metric" }
    }

    override fun send(graphiteMetrics: Collection<GraphiteMetric>) {
        logger.info { "Multi metric send started" }

        sender { socket ->
            graphiteMetrics.forEach {
                logger.info { "Sending out: $it" }
                socket.send(prepareMetric(it))
            }
        }

        logger.info { "Finished sending out multi metric to graphite" }
    }

    private fun sender(callback: (DatagramSocket) -> Unit) {
        val socket = openConnection()
        callback(socket)
        closeConnection(socket)
    }

    private fun prepareMetric(graphiteMetric: GraphiteMetric): DatagramPacket {
        val message = convertToSting(graphiteMetric)
        return DatagramPacket(message.toByteArray(), message.length, graphiteHostIp(), graphiteSettings.hostPort)
    }

    private fun convertToSting(metric: GraphiteMetric) =
            "${metric.metricName} ${metric.metricValue} ${metric.timestamp}"

    private fun graphiteHostIp() = InetAddress.getByName(graphiteSettings.hostUrl)

    private fun openConnection() = DatagramSocket(InetSocketAddress(InetAddress.getLocalHost(), 0))

    private fun closeConnection(socket: DatagramSocket) {
        socket.close()
    }
}
