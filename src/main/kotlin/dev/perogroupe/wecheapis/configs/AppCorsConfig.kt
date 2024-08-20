package dev.perogroupe.wecheapis.configs

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class AppCorsConfig : WebMvcConfigurer{

    @EnableWebMvc
    @Configuration
    class AppCorsConfig: WebMvcConfigurer{
        companion object{
            val allowedOrigins = arrayOf(
                "http://localhost:4200",
                "http://localhost:8080","*"
            )

            val allowedMethods = arrayOf(
                "GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"
            )
        }

        /**
         * Override method to add CORS mappings
         * @param registry the CorsRegistry
         */
        override fun addCorsMappings(registry: CorsRegistry) {
            // Allow origins and methods for "/api/v1/**"
            registry.addMapping("/api/v1/**")
                .allowedOrigins(*allowedOrigins)
                .allowedMethods(*allowedMethods)

            // Allow origins for "/v3/api-docs"
            registry.addMapping("/v3/api-docs").allowedOrigins("*")
        }
    }
}