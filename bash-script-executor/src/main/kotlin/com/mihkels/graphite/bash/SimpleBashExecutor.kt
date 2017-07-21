package com.mihkels.graphite.bash

import mu.KLogging
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

interface BashExecutor {
    fun runScript(scriptName: String): String
    fun runDirectory(directoryName: String): List<String>
}

class SimpleBashExecutor: BashExecutor {
    companion object: KLogging()

    override fun runScript(scriptName: String): String {
        val fullPath = getFileFullPath(scriptName)
        return executeScript(fullPath)
    }

    override fun runDirectory(directoryName: String): List<String> {
        val baseDirectory = getDirectory(directoryName)


        return Arrays.asList("Hello World")
    }

    private fun executeScript(fullPath: String): String {
        return ProcessExecutor().command(fullPath)
                .redirectOutput(Slf4jStream.of(javaClass).asInfo())
                .readOutput(true).execute().outputUTF8()
    }

    private fun getFileFullPath(scriptName: String): String {
        var localScriptName = Paths.get(scriptName)

        val resourceRoot = this::class.java.getResource("/")?.path
        localScriptName = if (Files.exists(localScriptName)) localScriptName
                          else Paths.get("$resourceRoot/$scriptName")

        if (Files.notExists(localScriptName)) {
            throw IOException("No such file $localScriptName make sure the BASH script path is correct")
        }

        return localScriptName.normalize().toString()
    }

    private fun getDirectory(directoryName: String): Path = Paths.get(".")
}