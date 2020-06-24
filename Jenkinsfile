pipeline {
  agent any
  stages {
    stage('Artifactory configuration') {
      // Tool name from Jenkins configuration
      rtGradle.tool = 'Gradle-2.4'
      // Set Artifactory repositories for dependencies resolution and artifacts deployment.
      rtGradle.deployer repo:'ext-release-local', server: server
      rtGradle.resolver repo:'remote-repos', server: server
    }

     stage('Gradle build') {
        buildInfo = rtGradle.run rootDir: "gradle-examples/4/gradle-example-ci-server/", buildFile: 'build.gradle.kts', tasks: 'clean artifactoryPublish'
    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }
  }
}
