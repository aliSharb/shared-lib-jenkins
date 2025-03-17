def call(String appDir) {
    echo "Building the Java application in ${appDir}..."

    // Ensure the directory exists before running Maven
    sh """
        if [ -d "${appDir}" ]; then
            cd ${appDir}
            mvn clean package
        else
            echo "Error: Directory ${appDir} does not exist!"
            exit 1
        fi
    """
}
