pipeline {
    agent any

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
            agent {
                docker {
                    image 'maven:3.6.3-jdk-14'
                }
                reuseNode true
            }

            steps {
                sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/${params.SUITE_XML} -DenvName=${params.ENVIRONMENT} -Dsubdomain=${params.DEV_SUBDOMAIN}"
            }
        }
    }

    post {
        always  {
            allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
        }

        cleanup {
            cleanWs()
        }
    }
}
