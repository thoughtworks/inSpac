<h1 align="center">
Gov Auth Component for<br> OpenID-Connect
</h1>
<p align="center">
<img  src="https://raw.githubusercontent.com/thoughtworks/GAC-OpenID-Connect/main/assets/images/GAC-OIDC-logo.png"
      height=30%
      width=30%>
<br>
<sup>
     Developed by ThoughtWorks, Inc.
</sup>
<br>
</p>

## About

**GAC** is the abbreviation of **Government Authentication Components**.

**GAC-OpenID-Connect** offers an SDK and a Keycloak plugin that can help developers fast integrate *OpenID-Connect 1.0* auth scheme into project to interact with Singapore government digital platform, such as *SingPass*.

GACs are implemented based on the official public documents: [SingPass Login Specifications](https://public.cloud.myinfo.gov.sg/sglogin/SingPass-login-specs-v0.1.html#section/Overview)

## Artifact & Usage
### SDK
[![SDK test](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/sdk-test.yaml/badge.svg)](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/sdk-test.yaml)

The SDK is for helping developers fast integrate with authentication scheme of SingPass platform.

To use the SDK, you have to import the SDK jar as a gradle dependency. For example, `build.gradle.kts` is configured as below.

``` groovy
dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    // All [.jar] files under [lib] folder are going to be imported as gradle dependencies
    
    // Other dependencies
    }
```

1. Download the latest version of SDK artifact (`com.thoughtworks.gac.oidc-sdk.jar`) at [GitHub Releases](https://github.com/thoughtworks/GAC-OpenID-Connect/releases/latest)
2. Create `lib` folder under the root directory of project (base on the above gradle configuration), put the SDK under `lib` folder.

📖 We also offer detailed **API documentations** for developers' reference: https://thoughtworks.github.io/GAC-OpenID-Connect/

📦 If you would like to check SDK documents / sources codes, please check [`sdk` folder](https://github.com/thoughtworks/GAC-OpenID-Connect/tree/main/sdk) to get more information.

### Keycloak Plugin
[![Keycloak plugin test](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/keycloak-plugin-test.yaml/badge.svg)](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/keycloak-plugin-test.yaml)

The Keycloak plugin only works on Keycloak. If you would like to use Keycloak as service provider and finish SingPass authentication with it, then this component is helpful.

📦 If you would like to check Keycloak plugin documents / sources codes, please check [`keycloak-plugin` folder](https://github.com/thoughtworks/GAC-OpenID-Connect/tree/main/keycloak-plugin) to get more information.


### Integration Sample
[![Integration sample test](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/sample-test.yaml/badge.svg)](https://github.com/thoughtworks/GAC-OpenID-Connect/actions/workflows/sample-test.yaml)

This is a demo project for demonstrating how to use SDK / Keycloak plugin. If you feel confused about using GACs, please check the [`sample` folder](https://github.com/thoughtworks/GAC-OpenID-Connect/tree/main/sample).


## Credit
This is project is licensed under [`MIT`](https://github.com/thoughtworks/GAC-OpenID-Connect/blob/main/LICENSE).

[MockPass](https://github.com/opengovsg/mockpass) is developed by GovTech, used to assist development and testing.

[Keycloak](https://www.keycloak.org) is used to assist developing Keycloak plugin.

[Dokka](https://github.com/Kotlin/dokka) is used to generate API documents. 