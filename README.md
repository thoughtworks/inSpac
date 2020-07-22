# SEA-SC-OIDC

A OIDC 1.0 integration SDK for SEA MU

## Documentation

- [How to config MockPass for OIDC](documents/How-To-Config-MockPass-For-OIDC.md)
- [Integration Demo of OIDC](documents/Integration-Demo-Of-OIDC.md)
- [More OIDC details](https://docs.google.com/presentation/d/1JNm5N8vuZvIMBCw3dVIunlKrXK7HhawMI5VDVyMf1cY/edit#slide=id.p1)


## Setup Project

### Switch Code Style

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

### Enable Git Hooks

To enable the pre-push hooks, use the `initGitHooks` Gradle task:

```
./gradlew initGitHooks
```

## Test and Build

To run the unit test, use the `test` Gradle task:

```
./gradlew test
```

To build the project, use the `build` Gradle task:

```
./gradlew build
```

## Generate SDK Documentation

The language used to document Kotlin code (the equivalent of Java's JavaDoc) is called KDoc. For more information check out [Documenting Kotlin Code](https://kotlinlang.org/docs/reference/kotlin-doc.html).

To generate the documentation, use the `dokka` Gradle task:

```
./gradlew dokka
```


## Dependencies & Plugins Requirement

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
jvm:1.3.71 |
org.jetbrains.dokka:0.10.1 |
org.jmailen.kotlinter:2.3.2 |
org.owasp.dependencycheck:5.3.2.1 | 
org.sonarqube:2.6.2 |


## Usage

To get the package, use the `build` gradle task:

```
./gradlew build
```

After the `build` gradle task, generate a package on `./build/libs/sea-oidc.jar`.
