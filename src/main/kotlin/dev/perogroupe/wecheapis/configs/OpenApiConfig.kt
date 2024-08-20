package dev.perogroupe.wecheapis.configs

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    /**
     * Defines the public API configuration.
     * This API is grouped under 'public' and scans packages under 'dev.perogroupe.wecheapis.controllers'.
     * It matches all paths.
     * @return GroupedOpenApi instance for the public API.
     */
    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .packagesToScan("dev.perogroupe.wecheapis.controllers")
            .pathsToMatch("/**")
            .build()
    }


}