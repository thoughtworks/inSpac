import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm") version "1.3.71"
    `java-library`
    `maven-publish`
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jmailen.kotlinter") version "2.3.2"
    id("org.owasp.dependencycheck") version "5.3.2.1"
}

group = "com.thoughtworks.gac"

base {
    archivesBaseName = "com.thoughtworks.gac.oidc-sdk"
}

apply {
    from("gradle/jacoco.gradle")
}

repositories {
    jcenter()
}

base {
    archivesBaseName = "com.thoughtworks.gac.oidc-sdk"
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework:spring-web:5.2.6.RELEASE")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    implementation("com.nimbusds:nimbus-jose-jwt:8.19")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.65")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("io.mockk:mockk:1.10.0")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/thoughtworks/GAC-OpenID-Connect")
            credentials {
                username = project.findProperty("github.package.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("github.package.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.thoughtworks.gac"
            artifactId = "oidc-sdk"
            version = project.findProperty("gac.oidc.version") as String? ?: System.getenv("GAC_SDK_VERSION")
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dependsOn("formatKotlin", "lintKotlin", "initGitHooks")
}

tasks.register<Copy>("initGitHooks") {
    from("config/githooks")
    into("./.git/hooks")
    // set file mode as 755
    fileMode = 0b000_111_101_101
}
tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("${buildDir}/jacocoHtml")
    }
    dependsOn(tasks.test) // tests are required to run before generating the report
}