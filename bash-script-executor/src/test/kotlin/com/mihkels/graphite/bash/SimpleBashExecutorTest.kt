package com.mihkels.graphite.bash

import org.assertj.core.api.KotlinAssertions.assertThat
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.IOException

internal class SimpleBashExecutorTest {
    @Rule @JvmField
    val exceptionRue: ExpectedException = ExpectedException.none()

    private var simpleBashExecutor = SimpleBashExecutor()

    @Before
    fun setUp() {
        simpleBashExecutor = SimpleBashExecutor()
    }

    @Test
    internal fun givenValidScriptWillRunItAndReturnHello() {
        val result = simpleBashExecutor.runScript("hello.sh")
        assertThat(result).isEqualTo("hello.world 10 1500627240927\n")
    }

    @Test
    internal fun givenInvalidScriptNameThrowsError() {
        val scriptName = "invalid_file.sh"
        exceptionRue.expect(IOException::class.java)
        exceptionRue.expectMessage(CoreMatchers.containsString(" make sure the BASH script path is correct") )
        simpleBashExecutor.runScript(scriptName)
    }

    @Test
    fun givenValidDirectoryRunsAllScriptsInsideAndReturnsResults() {
        val result = simpleBashExecutor.runDirectory("multi_script")
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0]).contains("ONE")
    }
}