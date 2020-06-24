pipeline {
  agent any
  stages {
    stage('Build') {
      steps{
        sh './gradlew clean build'
      }
    }

     stage('Document') {
      steps{
        sh './gradlew dokka'
      }
    }
  }

  post {
    always {
        archiveArtifacts artifacts: 'build/reports/tests/test/**/*', fingerprint: true
    }
    success {
      archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
      archiveArtifacts artifacts: 'build/dokka/**/*', fingerprint: true
    }
  }
}
