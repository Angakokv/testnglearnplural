pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-14'
        }
    }

    parameters {
        string(name: "DEV_SUBDOMAIN", defaultValue: 'Null', description: "Dev subdomain")
        choice(name: "ENVIRONMENT", choices: ['none', 'dev', 'test'], description: "Target environment")
        string(name: "SUITE_XML", defaultValue: 'api-tests.xml', description: "TestNG XML file")
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
                sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/${params.SUITE_XML} -DenvName=${params.ENVIRONMENT} -Dsubdomain=${params.DEV_SUBDOMAIN}"
            }
        }
    }

    post {
        cleanup {
            cleanWs()
        }
    }
}
