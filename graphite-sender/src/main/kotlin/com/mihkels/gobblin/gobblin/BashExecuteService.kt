package com.mihkels.gobblin.gobblin

import com.mihkels.graphite.bash.BashExecutor
import com.mihkels.graphite.client.GraphiteMetric
import mu.KLogging
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BashExecuteService(private val executorSettings: ScriptExecutorSettings, private val bashExecutor: BashExecutor) {
    companion object: KLogging()

    fun executeService(): List<GraphiteMetric> {
        return bashExecutor.runDirectory(executorSettings.scriptDirectory).map {
            GraphiteMetric("demo.metric.test", it.replace("\n", ""), Instant.now().toEpochMilli())
        }.toList()
    }
}