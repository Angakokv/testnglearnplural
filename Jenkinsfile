pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-14'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    stages {
        stage('Hello') {
            steps {
                echo "Running job '${env.JOB_NAME}', build-id '${env.BUILD_ID}', build url '${env.BUILD_URL}'"
                sh 'mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/api-tests.xml'
            }
        }
    }

    post {
        cleanup {
            cleanWs()
//             deleteDir()
        }
    }
}
