def call(String testDir) {
    echo "Running Unit Tests in: ${testDir}"

    sh """
        cd ${env.WORKSPACE}

        if [ ! -d "${testDir}" ]; then
            echo "Error: Test directory ${testDir} not found!"
            exit 1
        fi

        mvn -B test
    """
}
