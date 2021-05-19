# GAC-OIDC SDK

[![SDK test](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/sdk-test.yaml/badge.svg)](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/sdk-test.yaml)

## About
An SDK for fast integrating with SingPass platform on `OpenID Connect 1.0` auth scheme.

## Documentation

- [How to config MockPass for OIDC](documents/How-To-Config-MockPass-For-OIDC.md)
- [Integration Demo of OIDC](documents/Integration-Demo-Of-OIDC.md)
- [More OIDC details](https://docs.google.com/presentation/d/1JNm5N8vuZvIMBCw3dVIunlKrXK7HhawMI5VDVyMf1cY/edit#slide=id.p1)


## Project Setup

### Code Style

Switching to the Kotlin Coding Conventions code style can be done in `Preferences → Editor → Code Style → Kotlin` dialog. 

Switch scheme to *Project* and activate `Set from... → Kotlin Style Guide`.

To format the Kotlin code, use the `formatKotlin` Gradle task:

```
./gradlew formatKotlin
```

To check the Kotlin code style, use the `lintKotlin` Gradle task:

```
./gradlew lintKotlin
```

## Test & Build

To run the unit test, use the `test` Gradle task:

```
./gradlew test
```

To build the project, use the `build` Gradle task:

```
./gradlew build
```

## Usage

You can compile SDK by yourself or use the artifact directly.

### Preparation
No matter which way you choose to get the SDK, you always need to configure the gradle configuration to import the SDK to your project.

``` groovy
dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
 
    // Other dependencies
    }
```

The configuration snippet above means: all `.jar` files under `lib` folder are going to be imported as gradle dependencies

You can definitely modify configuration based on actual project requirements.

### Get artifacts
#### Selection 1: Compile
To get the package, use the `build` gradle task:

```
./gradlew build
```

After the `build` gradle task, you can get the SDK artifact at `./build/libs/com.thoughtworks.gac.oidc-sdk.jar`.

#### Selection 2: Download from GitHub Releases
1. Check the latest version of [Releases](https://github.com/thoughtworks/GAC-OpenID-Connect/releases/latest) at project homepage
2. Download the SDK (`com.thoughtworks.gac.oidc-sdk.jar`)

### Use the SDK
Put it under `lib` directory under the root of project folder (base on the gradle configuration in Preparation session).


## Dependencies
The project relies on dependencies and plugins below.

Dependency |
---- |
org.springframework:spring-web:5.2.6.RELEASE |
com.fasterxml.jackson.core:jackson-databind:2.11.0 |
org.bouncycastle:bcpkix-jdk15on:1.65 |
com.nimbusds:nimbus-jose-jwt:8.19 |
org.junit.jupiter:junit-jupiter:5.6.2 |
io.mockk:mockk:1.10.0 |


Plugin |
:---- |
kotlin:jvm:1.3.71 |
org.jetbrains.dokka:0.10.1 |
org.jmailen.kotlinter:2.3.2 |
org.owasp.dependencycheck:5.3.2.1 | 




