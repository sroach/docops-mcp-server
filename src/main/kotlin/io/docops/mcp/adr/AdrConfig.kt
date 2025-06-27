package io.docops.mcp.adr

import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdrConfig {

    @Bean
    fun adrToo(adrService: AdrService): ToolCallbackProvider {
        return  MethodToolCallbackProvider
            .builder()
            .toolObjects(adrService)
            .build()
    }
}