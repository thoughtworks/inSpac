package com.thoughtworks.sea.ssointegrationdemo

import com.thoughtworks.sea.ssointegrationdemo.model.property.OIDCProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(OIDCProperty::class)
class SSOIntegrationDemoApplication

fun main(args: Array<String>) {
    runApplication<SSOIntegrationDemoApplication>(*args)
}
