package com.mihkels.graphite.bash

import mu.KLogging
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

interface BashExecutor {
    fun runScript(scriptName: String): String
    fun runDirectory(directoryName: String): List<String>
}

class SimpleBashExecutor: BashExecutor {
    companion object: KLogging()
    private val fileHelpers = FileHelpers()

    override fun runScript(scriptName: String): String {
        val fullPath = fileHelpers.getPath(scriptName)
        return executeScript(fullPath.normalize().toString())
    }

    override fun runDirectory(directoryName: String): List<String> {
        val output: MutableList<String> = mutableListOf()
        fileHelpers.getFiles(directoryName).forEach {
            logger.info { "Executing script: $it" }
            output.add(executeScript(it))
        }

        return output
    }

    private fun executeScript(fullPath: String): String {
        return ProcessExecutor().command(fullPath)
                .redirectOutput(Slf4jStream.of(javaClass).asInfo())
                .readOutput(true).execute().outputUTF8()
    }
}

internal class FileHelpers {
    fun getPath(scriptName: String): Path {
        var localScriptName = Paths.get(scriptName)

        val resourceRoot = this::class.java.getResource("/")?.path
        localScriptName = if (Files.exists(localScriptName)) localScriptName
        else Paths.get("$resourceRoot/$scriptName")

        if (Files.notExists(localScriptName)) {
            throw IOException("No such file $localScriptName make sure the BASH script path is correct")
        }

        return localScriptName
    }

    fun getFiles(inputPath: String): List<String> {
        val scriptDirectoryPath = getPath(inputPath)
        var files: List<String> = emptyList()

        Files.walk(scriptDirectoryPath).use { paths ->
            files = paths.filter { path -> Files.isRegularFile(path) }
                    .map { it.toAbsolutePath().normalize().toString() }
                    .toList()
        }

        return files
    }
}
