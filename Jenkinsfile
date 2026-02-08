pipeline {
    agent any

    environment {
        HEADLESS = 'true'
    }

    // REMOVED THE 'tools' SECTION COMPLETELY
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Setup Permissions') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }

        stage('Run API Tests') {
            steps {
                echo 'Starting API Test Suite...'
                sh './gradlew clean test -Psuite=api'
            }
        }

        stage('Run UI Tests') {
            steps {
                echo 'Starting UI Test Suite...'
                sh './gradlew test -Psuite=ui'
            }
        }
    }

    post {
        always {
            echo 'Archiving Extent Reports...'
            archiveArtifacts artifacts: 'reports/*.html', fingerprint: true
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'API_ExtentReport.html, UI_ExtentReport.html',
                reportName: 'Automation Reports',
                reportTitles: 'API Results, UI Results'
            ])
        }
    }
}