package com.mihkels.graphite.client

import com.nhaarman.mockito_kotlin.*
import mu.KotlinLogging
import org.junit.Before
import org.junit.Test

private val logger = KotlinLogging.logger { }

class BasicGraphiteClientTest {
    lateinit var service: BasicGraphiteClient
    private lateinit var senderService: DataSender

    private val properties = GraphiteClientProperties()

    @Before
    fun setUp() {
        senderService = mock<DataSender> {
            on { sendData(any(), any(), any()) } doAnswer {
                logger.info { "${it.arguments[0]}, ${it.arguments[1]}" }
            }
        }

        service = BasicGraphiteClient(properties, senderService)
    }

    @Test
    fun givenSingleMetricWillCallSocketOnlyOnce() {
        service.send(listOf(GraphiteMetric("metric", "0")))
        verify(senderService).sendData(any(), any(), any())
    }

    @Test
    fun givenMultipleMetricsWillOnlyCallSocketONce() {
        service.send(listOf(GraphiteMetric(
                "metric", "0"),
                GraphiteMetric("second.metric", "2"))
        )

        verify(senderService, times(1)).sendData(any(), any(), any())
    }

    @Test
    fun givenEmptyCollectionNOSenderIsTriggered() {
        service.send(listOf())
        verify(senderService, times(0)).sendData(any(), any(), any())
    }
}
