package io.docops.mcp

import io.docops.mcp.featurecard.FeatureCardService
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

abstract class AbstractDocopsService {

    var restClient: RestClient = RestClient
        .builder()
        .baseUrl(BASE_URL)
        .build()

    companion object {
        val BASE_URL: String = "https://roach.gy"
        const val DOCOPS_SVG_ENDPOINT = "/extension/api/docops/svg"
        val KIND = "feature"
    }
    fun String.urlEncode(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8)
    }
    fun buildUri(payload: String,
                 scale: String = "1.0",
                 title: String = "Docops",
                 useDark: Boolean = false,
                 backend: String = "html5", kind: String): URI {
        val encodedPayload = payload.urlEncode()
        return  UriComponentsBuilder.fromPath(DOCOPS_SVG_ENDPOINT)
            .queryParam("kind", kind)
            .queryParam("payload", encodedPayload)
            .queryParam("scale", scale)
            .queryParam("type", "SVG")
            .queryParam("title", title)
            .queryParam("useDark", useDark)
            .queryParam("backend", backend)
            .build()
            .toUri()
    }

    fun getData(uri: URI) : String {
        val res = restClient.get()
            .uri(uri)
            .accept(MediaType.parseMediaType("image/svg+xml"))
            .retrieve()
            .body(ByteArray::class.java) ?: throw RuntimeException("Failed to get SVG response")
        return String(res, StandardCharsets.UTF_8)
    }
}

data class SvgResponse(val content: String, val mediaType: String = "image/svg+xml", val description: String = "Feature Card Response svg")
