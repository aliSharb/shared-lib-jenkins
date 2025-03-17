def call(String dockerImage, String dockerContext, String credentialsId) {
    script {
        stage('Build Docker Image') {
            echo "🔨 Building Docker image: ${dockerImage}..."
            sh "docker build -t ${dockerImage} ${dockerContext}"
        }

        stage('Push Docker Image to Docker Hub') {
            echo "📤 Pushing Docker image: ${dockerImage}..."
            withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                sh "docker push ${dockerImage}"
            }
        }

        stage('Clean Up Local Docker Image') {
            echo "🧹 Cleaning up local Docker image..."
            sh "docker rmi ${dockerImage} || true"
        }
    }
}
