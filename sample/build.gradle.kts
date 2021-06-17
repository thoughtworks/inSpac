import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.jmailen.kotlinter") version "2.3.2"
    id("org.owasp.dependencycheck") version "5.3.2.1"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
}

apply{
    from("gradle/jacoco.gradle")
}

group = "com.thoughtworks.inspac"
java.sourceCompatibility = JavaVersion.VERSION_1_8

base {
    archivesBaseName = "com.thoughtworks.inspac.integration-sample"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("commons-io:commons-io:2.6")
    implementation("com.nimbusds:nimbus-jose-jwt:8.19")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.65")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
    dependsOn("formatKotlin", "lintKotlin", "installGitHooks")
}

tasks.register<Copy>("installGitHooks") {
    from("config/githooks")
    into(".git/hooks")
    // Unix Permission = 0755
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

tasks.register<Exec>("buildImage") {
    workingDir("${project.projectDir}")
    commandLine("docker")
    args("build", "--build-arg", "PACKAGE_NAME=${project.name}-${version}.jar", "--rm=true", "-t", "${project.findProperty("image_name")}", ".")
    dependsOn("build")
}

tasks.register<Exec>("buildE2EImage") {
    workingDir("${project.projectDir}/E2E")
    commandLine("docker")
    args("build", "-t", "${project.findProperty("image_name")}", ".")
}