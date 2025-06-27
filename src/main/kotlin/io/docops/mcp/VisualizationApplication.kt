package io.docops.mcp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VisualizationApplication

fun main(args: Array<String>) {
	runApplication<VisualizationApplication>(*args)
}
