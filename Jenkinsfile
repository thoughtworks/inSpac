pipeline {
  agent any
    tools {
      maven 'maven-3.6.3'
  }
  stages {
    stage('Test & Build') {
      steps {
        sh 'mvn clean package'
      }
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
  }
}