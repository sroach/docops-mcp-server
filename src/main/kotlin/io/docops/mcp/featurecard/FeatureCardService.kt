package io.docops.mcp.featurecard

import io.docops.mcp.AbstractDocopsService
import io.docops.mcp.SvgResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class FeatureCardService : AbstractDocopsService() {


    companion object {
        private const val KIND = "feature"
    }
    @Tool(name="getFeatureCards", description = "Generates a List Feature Card Visualization")
    fun featureCard(@ToolParam(required = true, description = "A list of feature card data") featureCards: List<FeatureCardData>,
                    @ToolParam(description = "Can be one of LIGHT, DARK, AUTO") theme: String = "AUTO",
                    @ToolParam(description = "Can be one of GRID, COLUMN, ROW") layout: String = "GRID"
    ): SvgResponse {
        val payload = buildFeatureCardPayload(featureCards, theme, layout)
        return SvgResponse(createFeatureCard(payload).toString(StandardCharsets.UTF_8))
    }

    /**
     * Data class representing a feature card
     */
    data class FeatureCardData(
        val title: String,
        val description: String,
        val emoji: String,
        val colorScheme: String = "BLUE",
        val details: List<String> = emptyList()
    )

    /**
     * Creates a feature card SVG using the DocOpsRouter endpoint
     *
     * @param payload The feature card content in table format
     * @param scale Scale factor for the SVG (default: "1.0")
     * @param title Title for the feature card (default: "Feature Cards")
     * @param useDark Whether to use dark theme (default: false)
     * @param backend Backend type (default: "html5")
     * @return ByteArray containing the SVG response
     */
    fun createFeatureCard(
        payload: String,
        scale: String = "1.0",
        title: String = "Feature Cards",
        useDark: Boolean = false,
        backend: String = "html5"
    ): ByteArray {

        val uri = buildUri(payload, scale, title, useDark, backend, KIND)
        println(uri)
        return getData(uri).toByteArray()
    }
    private fun buildFeatureCardPayload(
        featureCards: List<FeatureCardData>,
        theme: String,
        layout: String
    ): String {
        val payload = StringBuilder()

        // Add configuration directives
        payload.appendLine("@theme: $theme")
        payload.appendLine("@layout: $layout")
        payload.appendLine()

        // Add table header
        payload.appendLine("Title | Description | Emoji | ColorScheme")

        // Add feature cards
        featureCards.forEach { card ->
            payload.appendLine("${card.title} | ${card.description} | ${card.emoji} | ${card.colorScheme}")

            // Add details if present
            card.details.forEach { detail ->
                payload.appendLine(">> $detail")
            }
        }

        return payload.toString()
    }



}