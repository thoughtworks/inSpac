pipeline {
  agent { label 'SSO-AGENT' }

  environment {
    APPLICATION_NAME = 'SEA-OIDC-Document'
    QA_HOST = '172.26.14.154'
    QA_USER = 'ubuntu'
    SSH_PORT = '63710'
    NETWORK_NAME = 'host'
    FILE_NAME = 'sea-oidc-document.tar'
    PUBLISH_PORT = '8280'

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
       steps{
          echo "==Build sea-oidc-document docker image=="
          sh "docker build -t ${IMAGE_NAME_TAGGED_LATEST} ."

          sh "docker images --filter=dangling=true | xargs docker rmi || true"
       }
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
          sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker run -p ${PUBLISH_PORT}:80 -d --name=${APPLICATION_NAME} --restart=always -e 'APP_ENV=qa' ${IMAGE_NAME_TAGGED_LATEST}\""

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
