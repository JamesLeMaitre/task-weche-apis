package dev.perogroupe.wecheapis.configs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File

@Component
@ConfigurationProperties(prefix = "file")
class FileConfig {

    var uploadDir: String = ""
        set(value) {
            field = value
            val directory = File(value)
            if (!directory.exists()) {
                directory.mkdirs()
            }
        }
}