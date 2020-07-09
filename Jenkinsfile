pipeline {

  environment {
    KEYCLOAK_CONTAINER_NAME = 'keycloak'
    KEYCLOAK_PLUGIN_PATH = '/opt/jboss/keycloak/standalone/deployments'
  }
  agent { label 'SSO-AGENT' }
    tools {
      maven 'maven-3.6.3'
  }
  stages {
    stage('Test & Build') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Deploy To Dev') {
      steps {
        timeout(time: 20, unit: 'SECONDS') {
          input message: 'Approve Deploy?', ok: 'Yes'
        }
        script {
          def files = findFiles(glob: 'target/*-with-dependencies.jar')
          def filePath = files[0].path
          def fileName = files[0].name
          sh 'docker cp '+ filePath +' ${KEYCLOAK_CONTAINER_NAME}:${KEYCLOAK_PLUGIN_PATH}/' + fileName
        }
      }
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'target/*-with-dependencies.jar', fingerprint: true
    }
  }


}

