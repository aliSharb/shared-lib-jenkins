def call(String KUBE_CONFIG_PATH, String KUBE_NAMESPACE, String KUBE_DEPLOYMENT_FILE, String BUILD_TAG) {
    script {
        try {
            // Validate if the deployment file exists
            if (!fileExists(KUBE_DEPLOYMENT_FILE)) {
                error "Deployment file ${KUBE_DEPLOYMENT_FILE} not found!"
            }

            echo "Updating deployment file with build tag: ${BUILD_TAG}"
            sh "sed -i 's/TAG_PLACEHOLDER/${BUILD_TAG}/g' ${KUBE_DEPLOYMENT_FILE}"

            echo "Applying deployment to namespace: ${KUBE_NAMESPACE}"
            sh "kubectl --kubeconfig=${KUBE_CONFIG_PATH} apply -n ${KUBE_NAMESPACE} -f ${KUBE_DEPLOYMENT_FILE}"

            echo "Checking rollout status for deployment..."
            int retries = 3
            int waitTime = 10 // Wait time in seconds before retrying

            for (int i = 0; i < retries; i++) {
                def status = sh(
                    script: "kubectl --kubeconfig=${KUBE_CONFIG_PATH} rollout status deployment my-app-deployment -n ${KUBE_NAMESPACE} || echo 'FAILED'",
                    returnStdout: true
                ).trim()

                if (status.contains("successfully rolled out")) {
                    echo "Deployment rolled out successfully!"
                    return
                } else {
                    echo "Rollout failed. Retrying in ${waitTime} seconds..."
                    sleep(waitTime)
                }
            }

            error "Deployment failed after ${retries} attempts."

        } catch (Exception e) {
            echo "❌ Error: ${e.message}"
            error "Deployment process failed!"
        }
    }
}
