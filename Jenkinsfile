pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo "Running job '${env.JOB_NAME}', build-id '${env.BUILD_ID}', build url '${env.BUILD_URL}'"
            }
        }
    }
}
