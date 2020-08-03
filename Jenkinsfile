pipeline {
    agent { label 'SSO-AGENT' }

    environment {
        APPLICATION_NAME = 'SEA-SC-Document'
        QA_HOST = '172.26.14.154'
        QA_USER = 'ubuntu'
        SSH_PORT = '63710'
        NETWORK_NAME = 'host'
        FILE_NAME = 'sea-sc-document.tar'
        NGINX_PUBLISH_PORT = '8280'
        SONAR_CREDS = credentials('SONAR')

        COMMIT = """${
            sh(
                    returnStdout: true,
                    script: 'git rev-parse --verify HEAD'
            )
        }""".trim()

        WORKSPACE = '/var/jenkins/workspace/SSO-OIDC'
        DOKKA_FOLDER = "${workspace}/build/dokka/sea-oidc"
        NGINX_CONTAINER_WORKSPACE = "/usr/share/nginx/html"

        IMAGE_NAME = "${APPLICATION_NAME}".toLowerCase()
        IMAGE_NAME_TAGGED_LATEST = "${IMAGE_NAME}:latest"

        SEA_SC_DOCUMENT_CONTAINER_EXIT = 0
    }

    stages {
        stage('CLEAN BUILD') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('DOCKER BUILD DOCUMENT') {
            steps {
                sh './gradlew dokka'
                script {
                    SEA_SC_DOCUMENT_CONTAINER_EXIT = sh(script: "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker inspect --type=container ${APPLICATION_NAME}\"", returnStatus: true)

                    if (SEA_SC_DOCUMENT_CONTAINER_EXIT == 1) {
                        sh "docker build -t ${IMAGE_NAME_TAGGED_LATEST} ."

                        sh "docker images --filter=dangling=true | awk '{print \$3}'| xargs docker rmi || true"
                    }
                }
            }

        }

        stage('DEPLOY DOCUMENT TO QA') {
            steps {
                script {
                    if (SEA_SC_DOCUMENT_CONTAINER_EXIT == 1) {
                        sh "docker save -o ${FILE_NAME} ${IMAGE_NAME_TAGGED_LATEST}"

                        sh "scp -P ${SSH_PORT} ${FILE_NAME} ${QA_USER}@${QA_HOST}:/home/ubuntu"

                        sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker stop ${APPLICATION_NAME} || true && docker rm ${APPLICATION_NAME} || true && docker rmi ${IMAGE_NAME_TAGGED_LATEST} || true\""

                        sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker images --filter=dangling=true| awk '{print \$3}' | xargs docker rmi || true \""

                        sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker load -i ${FILE_NAME} && docker run -p ${NGINX_PUBLISH_PORT}:80 -d --name=${APPLICATION_NAME} --restart=always -e 'APP_ENV=qa' ${IMAGE_NAME_TAGGED_LATEST}\""
                    } else {
                        sh "scp -r -P ${SSH_PORT} ${DOKKA_FOLDER} ${QA_USER}@${QA_HOST}:/home/ubuntu"

                        sh "ssh ${QA_USER}@${QA_HOST} -p ${SSH_PORT} \"docker exec -i ${APPLICATION_NAME} rm -rf  ${NGINX_CONTAINER_WORKSPACE}/sea-oidc && docker cp /home/ubuntu/sea-oidc ${APPLICATION_NAME}:${NGINX_CONTAINER_WORKSPACE} && rm -rf /home/ubuntu/sea-oidc\""
                    }
                }
            }
        }

        stage('SONAR ANALYSIS') {
            steps {
                sh './gradlew dependencyCheckAnalyze && ./gradlew sonarqube -Dsonar.host.url=http://${QA_HOST}:9000 -Dsonar.login=${SONAR_CREDS}'
            }
        }

        stage('UPDATE JAR FOR DEMO') {
            steps {
                sshagent(credentials: ['Jenkins-SSO-DEMO']) {
                    sh "cp /var/jenkins/workspace/SSO-OIDC/build/libs/sea-oidc.jar /var/jenkins/workspace/SSO-DEMO/lib/"
                    sh "cd /var/jenkins/workspace/SSO-DEMO && git add ./lib/sea-oidc.jar"
                    sh 'cd /var/jenkins/workspace/SSO-DEMO && git commit -m "[#135][Wei]: update the jar"'
                    sh "cd /var/jenkins/workspace/SSO-DEMO && git push git@github.com:ThoughtWorksInc/SEA-SC-Integration-Demo.git HEAD:master"
                }
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
