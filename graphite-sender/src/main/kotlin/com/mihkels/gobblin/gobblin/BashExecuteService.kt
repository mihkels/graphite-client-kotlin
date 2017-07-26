package com.mihkels.gobblin.gobblin

import com.mihkels.graphite.bash.BashExecutor
import com.mihkels.graphite.client.GraphiteMetric
import mu.KLogging
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BashExecuteService(
        private val executorSettings: ScriptExecutorSettings,
        private val bashExecutor: BashExecutor,
        private val metricsCreator: MetricsCreator
) {
    companion object: KLogging()

    fun executeService(): List<GraphiteMetric> {
        val collectedOutput = mutableListOf<GraphiteMetric>()
        bashExecutor.runDirectory(executorSettings.scriptDirectory, {output, scriptName ->
            collectedOutput.add(metricsCreator.createMetric(output, cleanupFallbackName(scriptName)))
        })

        return collectedOutput
    }

    private fun  cleanupFallbackName(fullScriptPath: String): String {
        val startPosition = fullScriptPath.lastIndexOf("/") + 1
        val endPositon = fullScriptPath.lastIndexOf(".") - 1

        return fullScriptPath.substring(startPosition..endPositon).replace("_", ".")
    }
}

@Service
class MetricsCreator {
    fun createMetric(inputLine: String, fallbackName: String): GraphiteMetric {
        val metricGroups = inputLine.replace("\n", "").split(" ")
        val metricName = if (metricGroups.size == 3) metricGroups[0] else fallbackName

        return if (metricGroups.size >= 2) {
            GraphiteMetric(metricName, metricGroups[1], metricGroups[2].toLong())
        } else {
            GraphiteMetric(metricName, metricGroups[0], Instant.now().toEpochMilli())
        }
    }
}
