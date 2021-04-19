## SSO Integration Demo
[![CircleCI](https://circleci.com/gh/twlabs/SEA-SC-Integration-Demo.svg?style=svg&circle-token=a5d3f691ed2c05ef3197b1e7dbca9a196590ec38)](https://circleci.com/gh/twlabs/SEA-SC-Integration-Demo)

### About Project
This project is a demo of SSO Integration.

#### Tech Stack
- Kotlin
- Spring Boot
- Spring Security
- Gradle
- Circle CI

#### Branches
Currently, project only has the `master` branch which contains the stable version of codes and releases.

Circle CI is also focus on `master` branch, it is supposed to automatically pull the latest commits from this repo, compile and deploy the artifacts to remote AWS Lightsail instance. 

#### Swagger UI
_Swagger UI 2_ has been integrated into the project for conveniently testing APIs.

Visit `http://localhost:8080/sso/swagger-ui.html` to access _Swagger UI_. You could change the _Swagger UI_ default URL by modifying context path configuration in the environment profile.

_Swagger UI_ related configurations are saved and controlled by `SwaggerConfiguration` class which is under the package `com.thoughtworks.sea.ssointegrationdemo.common.config.SwaggerConfiguration`.


### Commit Regulations

For high-efficiency team cooperation, unified coding standards and avoid service outages, 
several code inspection steps will be executed by Git Hook before you push commits to the remote repo.

⚠️ If any of the following steps fail, you will be unable to push your commits to remote repo.

1. Commit messages must match the format: `[#Card No.][Author]: Commit Message.`

      * `[#51][Shuchen]: Implement token verification feature. `
      * `[#52][Shuchen & Xiaomeng]: Remove unused Spring Data gradle dependencies.`

2. If commit message check passes, Git Hook would execute `formatKotlinMain` and `formatKotlinTest` gradle tasks to make sure all codes are format in a same code style before committing codes.
If codes successfully pass the gradle tasks, your commits will be saved into local staging area, ready for being pushed to remote repo.

3. Before you push commits to remote repo, Git Hook would execute `clean` and `build` gradle tasks to make sure your changes could pass all tests and be built successfully. 
If codes successfully pass the gradle tasks, all commits will be pushed to remote repo.

### Run the Application

To successfully run the Spring Boot application, you need to set the right environment profile.

All environment config profiles could be found under the folder `src/main/resources/`, the file name is like `application-[env name].yaml`.

The string between brackets `[ ]` is the environment name, (e.g., `local`, `dev`)

#### IntelliJ IDEA Ultimate
1. Import the project, open the `SSOIntegrationDemoApplication` located at `src/main/kotlin/com/thoughtworks/sea/ssointegrationdemo/SSOIntegrationDemoApplication.kt`
2. Click ▶️ icon on left of the main function `fun main(args: Array<String>)`
3. Click the last selection in the popup menu  `Create SSOIntegrationDemoA...`
4. In `Create Run Configuration: 'SSOIntegrationDemoApplication'` window, input the environment name in `Active Profile` text box.
5. Click OK to apply the Settings.
6. Run the Application.

#### IntelliJ IDEA Community
1. Import the project, open the `SSOIntegrationDemoApplication.kt` located at `src/main/kotlin/com/thoughtworks/sea/ssointegrationdemo/SSOIntegrationDemoApplication.kt`
2. Click ▶️ icon on left of the main function `fun main(args: Array<String>)`
3. Click the last selection in the popup menu  `Create SSOIntegrationDemoA...`
4. In `Create Run Configuration: 'SSOIntegrationDemoApplicationKt'` window, paste the command below in the `VM Options` text box and replace the `ENV_NAME` with the environment name that you would like to select:

    ```
   -Dspring.profiles.active=ENV_NAME
    ```
5. Click OK to apply the Settings.
6. Run the Application.

### mockPass local config

to setup your mockPass, you can follow this list:

1. go to [mockPass Gitpod](https://gitpod.io/#https://github.com/opengovsg/mockpass)
2. login with your github account and open a new workspace
3. navigate to lib/express/oidc.js file add a params in line 81:
    `jose.JWE.createEncrypt({format: "compact"}, encryptionKey)` 
4. waiting for online server setup and you can click open browser button on right-bottom to open your mockPass
5. copy your url into resources/application-local.yaml file to replace mockpass.host

### how to integration SDK

1. import `sea-oidc.jar`, put in `./lib` directory.


### Dependencies & Plugins Requirement

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
org.sonarqube:2.6.2 |
org.owasp.dependencycheck:5.3.2.1 |
kotlin:jvm:1.3.72 |
kotlin:plugin.spring:1.3.72 |
kotlin:plugin.jpa:1.3.72 |
