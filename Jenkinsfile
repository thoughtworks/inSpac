pipeline {
  agent any

  environment {
    APPLICATION_NAME = 'SEA-OIDC-Document'
    QA_HOST = '172.26.14.154'
    QA_USER = 'ubuntu'
    SSH_PORT = '63710'
    NETWORK_NAME = 'host'

    IMAGE_NAME = "${APPLICATION_NAME}".toLowerCase()
    IMAGE_NAME_TAGGED_LATEST = "${IMAGE_NAME}:latest"
  }

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

     stage('Docker build'){
       echo "==Build sea-oidc docker image=="
       sh 'docker build -t ${IMAGE_NAME_TAGGED_LATEST} .'
     }

     stage('Deploy to QA') {
       steps{
          sh "docker save -o ${FILE_NAME} ${IMAGE_NAME_TAGGED_LATEST}"

          sh "scp -P ${SSH_PORT} ${FILE_NAME} ${QA_USER}@${QA_HOST}:/home/ubuntu"

          sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker stop ${APPLICATION_NAME} || true && docker rm ${APPLICATION_NAME} || true && docker rmi ${IMAGE_NAME_TAGGED_LATEST} || true\""
          script {
            try {
               sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker images --filter=dangling=true | xargs docker rmi || true \""
            } catch (Exception e) {
               echo "not found none tag images"
              }
            }

          sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker load -i ${FILE_NAME}\""
          sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker run -d --name=${APPLICATION_NAME} --restart=always --net=${NETWORK_NAME} -e'APP_ENV=qa' ${IMAGE_NAME_TAGGED_LATEST}\""

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
