package com.fractalwrench.json2kotlin

import com.bugsnag.Bugsnag
import org.apache.commons.cli.*
import java.io.File
import java.nio.file.Paths


fun main(args: Array<String>) {
    Bugsnag("36024661eca71fb467ceddbaeeff35c3")

    val options = prepareOptions()
    val parser = DefaultParser()

    try {
        val cmd = parser.parse(options, args)

        if (cmd.hasOption("help") || !cmd.hasOption("input")) {
            printHelp(options)
        } else {
            val parsedOptionValue = cmd.getParsedOptionValue("input") as String
            val inputFile = Paths.get(parsedOptionValue).toFile()

            if (inputFile.exists()) {
                val outputFile = findOutputFile(inputFile)
                Kotlin2JsonConverter().convert(inputFile.inputStream(), outputFile.outputStream(), ConversionArgs())
                println("Generated source available at '$outputFile'")
            } else {
                println("Failed to find file '$inputFile'")
            }
        }
    } catch (e: ParseException) {
        println("Failed to parse arguments: ${e.message}")
    }
}

private fun findOutputFile(inputFile: File): File {
    val name = inputFile.nameWithoutExtension
    val path = inputFile.parentFile.absolutePath
    return File(path, "$name.kt")
}

private fun printHelp(options: Options) {
    val formatter = HelpFormatter()
    formatter.printHelp("json2kotlin", options)
}

private fun prepareOptions(): Options {
    return with(Options()) {
        addOption(Option.builder("input")
                .desc("The JSON file input")
                .numberOfArgs(1)
                .build())
        addOption(Option.builder("packageName")
                .desc("The package name for the generated Kotlin file")
                .numberOfArgs(1)
                .build())
        addOption(Option.builder("help")
                .desc("Displays help on available commands")
                .build())
    }
}