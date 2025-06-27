package io.docops.mcp.featurecard

import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeatureCardConfig {

    @Bean
    fun featureCard(featureCardService: FeatureCardService): ToolCallbackProvider {
        return MethodToolCallbackProvider
            .builder()
            .toolObjects(featureCardService)
            .build()
    }
}