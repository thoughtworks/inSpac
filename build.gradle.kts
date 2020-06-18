import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.71"
    `java-library`
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jmailen.kotlinter") version "2.3.2"
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework:spring-web:5.2.6.RELEASE")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("io.mockk:mockk:1.10.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("formatKotlin", "lintKotlin", "initGitHooks")
}

tasks.register<Copy>("initGitHooks") {
    from("config/githooks")
    into("./.git/hooks")
    // set file mode as 755
    fileMode = 0b000_111_101_101
}
