package com.mihkels.graphite.bash

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

data class BashExecutorSettings(val scriptDirectory: String = "scripts")

interface BashExecutor {
    fun runScript(scriptName: String): String
    fun runDirectory(directoryName: String): List<String>
}

class SimpleBashExecutor(private val bashExecutorSettings: BashExecutorSettings): BashExecutor {
    override fun runScript(scriptName: String): String {
        if (Files.exists(Paths.get(scriptName)).not()) {
            throw IOException("No such file $scriptName make sure the BASH script path is correct")
        }

        return "Hello World"
    }

    override fun runDirectory(directoryName: String): List<String> {
        val baseDirectory = getDirectory(directoryName)
        return Arrays.asList("Hello World")
    }

    private fun getDirectory(directoryName: String): Path = Paths.get(".")
}