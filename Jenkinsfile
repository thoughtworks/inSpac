def SEA_SC_DOCUMENT_CONTAINER_EXIT = 0

pipeline {
  agent { label 'SSO-AGENT' }

  environment {
    APPLICATION_NAME = 'SEA-SC-Document'
    QA_HOST = '172.26.14.154'
    QA_USER = 'ubuntu'
    SSH_PORT = '63710'
    NETWORK_NAME = 'host'
    FILE_NAME = 'sea-sc-document.tar'
    PUBLISH_PORT = '8280'
    SONAR_CREDS = credentials('SONAR')

    WORKSPACE = '/var/jenkins/workspace/SSO-OIDC'
    DOKKA_FOLDER = "${workspace}/build/dokka/sea-oidc"

    IMAGE_NAME = "${APPLICATION_NAME}".toLowerCase()
    IMAGE_NAME_TAGGED_LATEST = "${IMAGE_NAME}:latest"

    SEA_SC_DOCUMENT_CONTAINER_EXIT = 0
  }

  stages {
    stage('Build') {
      steps{
        sh './gradlew clean build'
      }
    }

    stage('SONAR ANALYSIS') {
          steps {
              script {
                  sh './gradlew sonarqube -x test -Dsonar.host.url=http://${QA_HOST}:9000 -Dsonar.login=${SONAR_CREDS}'
              }
          }
    }

    stage('Document') {
       steps{
         sh './gradlew dokka'
       }
     }

     stage('Docker build'){
         steps{
             script {
                SEA_SC_DOCUMENT_CONTAINER_EXIT = sh(script: "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker inspect --type=container ${APPLICATION_NAME}\"", returnStatus: true)
                echo "SEA_SC_DOCUMENT_CONTAINER_EXIT value: ${SEA_SC_DOCUMENT_CONTAINER_EXIT}"

                if ( SEA_SC_DOCUMENT_CONTAINER_EXIT == 0 ){
                        sh "scp -r -P ${SSH_PORT} ${DOKKA_FOLDER} ${QA_USER}@${QA_HOST}:/home/ubuntu"

                        sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker cp /home/ubuntu/sea-oidc ${APPLICATION_NAME}:/usr/share/nginx/html\""

                        sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker stop ${APPLICATION_NAME} || true && docker restart ${APPLICATION_NAME}\""

                    }else{
                       sh "docker build -t ${IMAGE_NAME_TAGGED_LATEST} ."

                       sh "docker images --filter=dangling=true | xargs docker rmi || true"
                 }
             }
         }

     }

     stage('Deploy to QA') {
        when {
           expression {
               SEA_SC_DOCUMENT_CONTAINER_EXIT == 1
           }
        }
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
