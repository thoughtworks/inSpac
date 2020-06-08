# SEA-SC-OIDC

A OIDC 1.0 integration SDK for SEA MU

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
