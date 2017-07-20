package com.mihkels.graphite.bash

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SimpleBashExecutorTest {
    private val executorSettings = BashExecutorSettings(".")
    private var simpleBashExecutor = SimpleBashExecutor(executorSettings)

    @BeforeEach
    internal fun setUp() {
        simpleBashExecutor = SimpleBashExecutor(executorSettings)
    }

    @Test
    internal fun happyPathExecutionTest() {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}