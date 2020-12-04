pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-14'
        }
    }

    parameters {
        string(name: "DEV_SUBDOMAIN", defaultValue: 'Null', description: "Dev subdomain")
        choice(name: "ENVIRONMENT", choices: ['none', 'dev', 'test'], description: "Target environment")
    }

    options {
        ansiColor('xterm')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Test') {
            steps {
                echo "Running job '${env.JOB_NAME}', build-id '${env.BUILD_ID}', build url '${env.BUILD_URL}'"
                sh 'mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/api-tests.xml -DenvName=${params.ENVIRONMENT} -Dsubdomain=${params.DEV_SUBDOMAIN}'
            }
        }
    }

    post {
        cleanup {
            cleanWs()
        }
    }
}
