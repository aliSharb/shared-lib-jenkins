def call(String appDir) {
    echo "Running Unit Tests in: ${appDir}"

    // Navigate to the application directory and execute Maven tests
    sh """
        cd ${env.WORKSPACE}/${appDir}
        if [ ! -f "pom.xml" ]; then
            echo "No pom.xml found in ${env.WORKSPACE}/${appDir}!"
            exit 1
        fi
        mvn test
    """
}
