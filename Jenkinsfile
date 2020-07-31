pipeline {

  environment {
    KEYCLOAK_CONTAINER_NAME = 'keycloak'
    KEYCLOAK_PLUGIN_PATH = '/opt/jboss/keycloak/standalone/deployments'
    QA_HOST = '172.26.14.154'
    QA_USER = 'ubuntu'
    SSH_PORT = '63710'
    NETWORK_NAME = 'host'
    FILE_NAME = getFileName()
    FILE_PATH = getFilePath()
  }
  agent { label 'SSO-AGENT' }
    tools {
      maven 'maven-3.6.3'
  }
  stages {
    stage('TEST & BUILD') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('DEPLOY TO DEV') {
      steps {
        script {
          sh 'docker cp ${FILE_PATH} ${KEYCLOAK_CONTAINER_NAME}:${KEYCLOAK_PLUGIN_PATH}/${FILE_NAME}'
        }
      }
    }
    stage('DEPLOY TO QA') {
      steps {
        timeout(time: 20, unit: 'SECONDS') {
          input message: 'Approve Deploy To Qa?', ok: 'Yes'
        }
        script {
          sh "scp -r -P ${SSH_PORT} ${FILE_PATH} ${QA_USER}@${QA_HOST}:/home/ubuntu"
          sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker cp /home/ubuntu/${FILE_NAME} ${KEYCLOAK_CONTAINER_NAME}:${KEYCLOAK_PLUGIN_PATH}\""
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

def getFile(){
    return findFiles(glob: 'target/*-with-dependencies.jar')[0]
}

def getFileName() {
    return getFile().name
}

def getFilePath() {
    return getFile().path
}
