# inSpac Integration Sample
[![Integration sample test](https://github.com/thoughtworks/inSpac/actions/workflows/sample-test.yaml/badge.svg)](https://github.com/thoughtworks/inSpac/actions/workflows/sample-test.yaml)

## About
This project is a demo project for demonstrating the way to integrate SDK / Keycloak plugin into your project.

### Tech Stack
- Kotlin
- Spring Boot
- Spring Security
- Gradle

## Project Setup
### Import SDK
The sample project doesn't contain the SDK / Keycloak Plugin, to run the project locally, you have to manually import the SDK first.

In this project, the `build.gradle.kts` is configured as below. It means all `.jar` files which are under `lib` folder are going to be imported as dependencies.
``` groovy
dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    // Other dependencies
    }
```

1. Check the latest version of [Releases](https://github.com/thoughtworks/inSpac/releases/latest) at project homepage
2. Create `lib` folder under the `sample` folder (base on the above gradle configuration), put the SDK under `lib` folder.

## Run Application

To successfully run the Spring Boot application, you need to set the right environment profile.

All environment config profiles could be found under the folder `src/main/resources/`, the file name is like `application-[env name].yaml`.

The string between brackets `[ ]` is the environment name, (e.g., `local`, `dev`)

#### IntelliJ IDEA Ultimate
1. Import the project, open the `InspacIntegrationSampleApplication` located at `src/main/kotlin/com/thoughtworks/inspac/sample/InspacIntegrationSampleApplication.kt`
2. Click ▶️ icon on left of the main function `fun main(args: Array<String>)`
3. Click the last selection in the popup menu  `Create InspacIntegrationSampleA...`
4. In `Create Run Configuration: 'InspacIntegrationSampleApplication'` window, input the environment name in `Active Profile` text box.
5. Click OK to apply the Settings.
6. Run the Application.

#### IntelliJ IDEA Community
1. Import the project, open the `InspacIntegrationSampleApplication.kt` located at `src/main/kotlin/com/thoughtworks/inspac/integration/sample/InspacIntegrationSampleApplication.kt`
2. Click ▶️ icon on left of the main function `fun main(args: Array<String>)`
3. Click the last selection in the popup menu  `Create InspacIntegrationSampleA...`
4. In `Create Run Configuration: 'InspacIntegrationSampleApplicationKt'` window, paste the command below in the `VM Options` text box and replace the `ENV_NAME` with the environment name that you would like to select:

    ```
   -Dspring.profiles.active=ENV_NAME
    ```
5. Click OK to apply the Settings.
6. Run the Application.

### Swagger UI
_Swagger UI 2_ has been integrated into the project for conveniently testing APIs.

Visit `http://localhost:8080/inspac/swagger-ui.html` to access _Swagger UI_. You could change the _Swagger UI_ default URL by modifying context path configuration in the environment profile.

_Swagger UI_ related configurations are saved and controlled by `SwaggerConfiguration` class which is under the package `com.thoughtworks.inspac.integration.sample.common.config.SwaggerConfiguration`.


### MockPass local config

to setup your MockPass, you can follow this list:

1. Go to [MockPass GitPod](https://gitpod.io/#https://github.com/opengovsg/mockpass)
2. Login with your GitHub account and open a new workspace
3. Navigate to lib/express/oidc.js file add a params in line 81:
    `jose.JWE.createEncrypt({format: "compact"}, encryptionKey)` 
4. Waiting for online server setup and you can click open browser button on right-bottom to open your MockPass
5. Copy your url into resources/application-local.yaml file to replace mockpass.host

## Dependencies

The project relies on dependencies and plugins below.

Dependency |
:---- |
org.springframework.boot:spring-boot-starter-web |
org.springframework.boot:spring-boot-starter-security |
com.fasterxml.jackson.module:jackson-module-kotlin |
org.jetbrains.kotlin:kotlin-reflect |
org.jetbrains.kotlin:kotlin-stdlib-jdk8 |
io.springfox:springfox-swagger2:2.9.2 |
io.springfox:springfox-swagger-ui:2.9.2 |
commons-io:commons-io:2.6 |
com.nimbusds:nimbus-jose-jwt:8.19 |
org.bouncycastle:bcpkix-jdk15on:1.65 |
org.springframework.boot:spring-boot-starter-thymeleaf |
org.springframework.boot:spring-boot-configuration-processor |
org.junit.jupiter:junit-jupiter:5.6.2 |
io.mockk:mockk:1.10.0 |
org.springframework.boot:spring-boot-starter-test |


Plugin |
:---- |
org.springframework.boot:2.3.0.RELEASE |
io.spring.dependency-management:1.0.9.RELEASE |
org.jmailen.kotlinter:2.3.2 | 
org.owasp.dependencycheck:5.3.2.1 |
kotlin:jvm:1.3.72 |
kotlin:plugin.spring:1.3.72 |
kotlin:plugin.jpa:1.3.72 |
