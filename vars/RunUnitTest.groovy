def call(String testDir) {
    echo "Running Unit Tests in: ${testDir}"

    sh """
        cd ${env.WORKSPACE}/${testDir}

        if [ ! -d "." ]; then
            echo "Error: Test directory ${testDir} not found!"
            exit 1
        fi

        cd ${env.WORKSPACE}  # Go back to root project directory
        mvn test
    """
}
