pipeline {
  agent any
  stages {
    stage('Build') {
      sh './gradlew clean build'
    }

     stage('Document') {
      sh './gradlew dokka'
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
      archiveArtifacts artifacts: 'build/dokka', fingerprint: true
    }
    always {
      junit 'build/reports/**/*.xml'
    }
  }
}
