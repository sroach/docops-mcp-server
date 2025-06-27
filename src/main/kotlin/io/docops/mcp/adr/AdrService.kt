package io.docops.mcp.adr

import io.docops.mcp.AbstractDocopsService
import io.docops.mcp.SvgResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.isNotEmpty

@Service
class AdrService: AbstractDocopsService() {

    companion object {
        private const val KIND = "adr"
    }

    @Tool(name = "adrMaker", description = "A tool for creating Architecture Decision Records (ADR)")
    fun makeAdr(@ToolParam(required = true, description = "The architecture decision record data") adr: AdrData) : SvgResponse {
        val payload = convertToTextFormat(adr)
        val uri = buildUri(payload, title=adr.title, kind= KIND)
        return SvgResponse(getData(uri))
    }

    /**
     * Data class representing an Architecture Decision Record (ADR)
     */
    data class AdrData(
        val title: String,
        val date: LocalDate,
        val status: AdrStatus,
        val context: List<String>,
        val decision: List<String>,
        val consequences: List<String>,
        val participants: List<String> = emptyList(),
        val references: List<String> = emptyList()
    )
    enum class AdrStatus {
        PROPOSED,
        ACCEPTED,
        SUPERSEDED,
        DEPRECATED,
        REJECTED
    }

    // Enhanced participant data class for detailed information
    data class ParticipantData(
        val name: String,
        val title: String,
        val email: String,
        val color: String,
        val emoji: String
    )

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Converts AdrData to the custom text format
     */
    fun convertToTextFormat(
        adrData: AdrData,
        contextDescription: String = "",
        decisionDescription: String = "",
        consequencesDescription: String = "",
        participantsData: List<ParticipantData> = emptyList()
    ): String {
        val sb = StringBuilder()

        // Title
        sb.appendLine("title: ${adrData.title}")

        // Status (capitalize first letter, lowercase rest)
        sb.appendLine("status: ${adrData.status.name.lowercase().replaceFirstChar { it.uppercase() }}")

        // Date
        sb.appendLine("date: ${adrData.date.format(dateFormatter)}")

        // Context with optional description
        if (contextDescription.isNotEmpty()) {
            sb.appendLine("context: $contextDescription")
        } else {
            sb.appendLine("context:")
        }
        adrData.context.forEach { context ->
            sb.appendLine("- $context")
        }

        // Decision with optional description
        if (decisionDescription.isNotEmpty()) {
            sb.appendLine("decision: $decisionDescription")
        } else {
            sb.appendLine("decision:")
        }
        adrData.decision.forEach { decision ->
            sb.appendLine("- $decision")
        }

        // Consequences with optional description
        if (consequencesDescription.isNotEmpty()) {
            sb.appendLine("consequences: $consequencesDescription")
        } else {
            sb.appendLine("consequences:")
        }
        adrData.consequences.forEach { consequence ->
            sb.appendLine("- $consequence")
        }

        // Participants with detailed format (no dashes, pipe-separated)
        if (participantsData.isNotEmpty()) {
            sb.appendLine("participants:")
            participantsData.forEach { participant ->
                sb.appendLine("${participant.name} | ${participant.title} | ${participant.email} | ${participant.color} | ${participant.emoji}")
            }
        } else if (adrData.participants.isNotEmpty()) {
            // Fallback to simple participant format
            sb.appendLine("participants:")
            adrData.participants.forEach { participant ->
                sb.appendLine(participant)
            }
        }

        // References (no dashes, direct wiki links)
        if (adrData.references.isNotEmpty()) {
            sb.appendLine("references:")
            adrData.references.forEach { reference ->
                // If reference is already in wiki link format, use as-is
                if (reference.startsWith("[[") && reference.endsWith("]]")) {
                    sb.appendLine(reference)
                } else {
                    // Convert simple URL to wiki link format
                    sb.appendLine("[[${reference}]]")
                }
            }
        }

        return sb.toString()
    }

}