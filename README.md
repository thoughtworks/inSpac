<h1 align="center">
      inSpac
</h1>
<p align="center">
      <img  src="assets/images/inSpac-logo.png"
      height=10%
      width=10%>
<br><br>
<sup>
     Developed by Thoughtworks, Inc.
</sup>
<br>
</p>

## About

**inSpac** is the abbreviation for **in Singpass Authentication Components**, developed and maintained by _Thoughtworks, Inc_.

**inSpac** offers an SDK and a Keycloak plugin that can help developers fast integrate *OpenID-Connect 1.0* auth scheme into project to interact with Singapore government digital platform -- *Singpass*.

inSpac is implemented based on the official public documents: [For Developers {Login} / Overview](https://api.singpass.gov.sg/library/login/developers/overview-at-a-glance)

## Artifacts & Usage
### SDK
[![SDK test](https://github.com/thoughtworks/inSpac/actions/workflows/sdk-test.yaml/badge.svg)](https://github.com/thoughtworks/inSpac/actions/workflows/sdk-test.yaml)

The SDK is for helping developers fast integrate with authentication scheme of Singpass platform.

To use the SDK, you have to import the SDK jar as a gradle dependency. For example, `build.gradle.kts` is configured as below.

``` groovy
dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    // All [.jar] files under [lib] folder are going to be imported as gradle dependencies
    
    // Other dependencies
    }
```

1. Download the latest version of SDK artifact (`com.thoughtworks.inspac.sdk.jar`) at [GitHub Releases](https://github.com/thoughtworks/inSpac/releases/latest)
2. Create `lib` folder under the root directory of project (base on the above gradle configuration), put the SDK under `lib` folder.

📖 We also offer detailed **API documentations** for developers' reference: https://thoughtworks.github.io/inSpac/

📦 If you would like to check SDK documents / sources codes, please check [`sdk` folder](https://github.com/thoughtworks/inSpac/tree/main/sdk) to get more information.

### Keycloak Plugin
[![Keycloak plugin test](https://github.com/thoughtworks/inSpac/actions/workflows/keycloak-plugin-test.yaml/badge.svg)](https://github.com/thoughtworks/inSpac/actions/workflows/keycloak-plugin-test.yaml)

The Keycloak plugin only works on Keycloak. If you would like to use Keycloak as service provider and finish Singpass authentication with it, then this component is helpful.

📦 If you would like to check Keycloak plugin documents / sources codes, please check [`keycloak-plugin` folder](https://github.com/thoughtworks/inSpac/tree/main/keycloak-plugin) to get more information.


### Integration Sample
[![Integration sample test](https://github.com/thoughtworks/inSpac/actions/workflows/sample-test.yaml/badge.svg)](https://github.com/thoughtworks/inSpac/actions/workflows/sample-test.yaml)

This is a demo project for demonstrating how to use SDK / Keycloak plugin. If you feel confused about using inSpac, please check the [`sample` folder](https://github.com/thoughtworks/inSpac/tree/main/sample).


## Credits
The project is licensed under [`MIT`](https://github.com/thoughtworks/inSpac/blob/main/LICENSE).

* [Singpass](https://www.singpass.gov.sg/) logo, brand, shape and all related services are owned by _Government of Singapore_.
* All Singpass spellings strictly follow the official guidelines: [_Logo download & Brand guidelines - Naming Usage session_](https://api.singpass.gov.sg/library/myinfo/business/implementation-display-guidelines).
* [Keycloak](https://www.keycloak.org) is an open source project, owned by _Red hat_.
* [MockPass](https://github.com/opengovsg/mockpass) is an open-source project, owned by _GovTech_.
* [Dokka](https://github.com/Kotlin/dokka) is used to generate API documents. 
