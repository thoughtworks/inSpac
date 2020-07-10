pipeline {

  environment {
    KEYCLOAK_CONTAINER_NAME = 'keycloak'
    KEYCLOAK_PLUGIN_PATH = '/opt/jboss/keycloak/standalone/deployments'
    QA_HOST = '172.26.14.154'
    QA_USER = 'ubuntu'
    SSH_PORT = '63710'
    NETWORK_NAME = 'host'
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
          input message: 'Approve Deploy To Dev?', ok: 'Yes'
        }
        script {
          def files = findFiles(glob: 'target/*-with-dependencies.jar')
          def filePath = files[0].path
          def fileName = files[0].name
          sh 'docker cp '+ filePath +' ${KEYCLOAK_CONTAINER_NAME}:${KEYCLOAK_PLUGIN_PATH}/' + fileName
        }
      }
    }
    stage('Deploy To Qa') {
      steps {
        timeout(time: 20, unit: 'SECONDS') {
          input message: 'Approve Deploy To Qa?', ok: 'Yes'
        }
        script {
          def files = findFiles(glob: 'target/*-with-dependencies.jar')
          def filePath = files[0].path
          def fileName = files[0].name
          sh "scp -r -P ${SSH_PORT} ${filePath} ${QA_USER}@${QA_HOST}:/home/ubuntu"
          sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker cp /home/ubuntu/${fileName} ${KEYCLOAK_CONTAINER_NAME}:${KEYCLOAK_PLUGIN_PATH}\""
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

