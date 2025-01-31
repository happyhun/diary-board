pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/happyhun/diary-board.git'
        BRANCH = 'main'
        DOCKER_IMAGE = 'diary-board-image'
        DOCKER_CONTAINER = 'diary-board-container'
        SERVER_USER = 'ubuntu'
        SERVER_HOST = 'http://ec2-15-164-164-252.ap-northeast-2.compute.amazonaws.com'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: "${BRANCH}", url: "${REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh './gradlew clean test'
                }
            }
            post {
                failure {
                    error('Test stage failed. Stopping the pipeline.')
                }
            }
        }

        stage('Docker Build & Run') {
            steps {
                script {
                    sh """
                    if docker ps -q --filter "name=${DOCKER_CONTAINER}" | grep -q .; then docker stop ${DOCKER_CONTAINER} && docker rm ${DOCKER_CONTAINER}; fi
                    docker rm ${DOCKER_CONTAINER} || true
                    docker build -t ${DOCKER_IMAGE} .
                    docker run -d -p 8080:8080 --name ${DOCKER_CONTAINER} ${DOCKER_IMAGE}
                    """
                }
            }
        }
    }
}
