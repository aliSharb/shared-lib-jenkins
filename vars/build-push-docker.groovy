def call(String dockerImage, String dockerContext, String credentialsId) {
    stage('Build Docker Image') {
        script {
            sh "docker build -t ${dockerImage} ${dockerContext}"
        }
    }
    stage('Push Docker Image to Docker Hub') {
        script {
            withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                sh "docker push ${dockerImage}"
            }
        }
    }
    stage('Clean Up Local Docker Image') {
        script {
            sh "docker rmi ${dockerImage}"
        }
    }
}
