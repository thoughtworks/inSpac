package com.thoughtworks.inspac.integration.sample

import com.thoughtworks.inspac.integration.sample.model.property.OIDCProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(OIDCProperty::class)
class InspacIntegrationSample

fun main(args: Array<String>) {
    runApplication<InspacIntegrationSample>(*args)
}
