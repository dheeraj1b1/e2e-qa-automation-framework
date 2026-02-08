pipeline {
    agent any

    environment {
        HEADLESS = 'true'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean Workspace') {
            steps {
                // Wipe the reports directory clean before starting
                // This prevents 'Ghost Reports' from previous builds appearing
                sh 'rm -rf reports/*' 
                sh 'chmod +x ./gradlew'
            }
        }

        stage('Run ReqRes API Tests') {
            steps {
                echo 'Starting ReqRes API Test Suite (CI-Safe)...'
                // Run only the API-CI suite
                sh './gradlew clean test -Psuite=api-ci'
            }
        }

        stage('Run UI Tests') {
            steps {
                echo 'Starting UI Test Suite...'
                // Run only the UI suite (without 'clean' to preserve API report)
                sh './gradlew test -Psuite=ui'
            }
        }
    }

    post {
        always {
            echo 'Archiving Extent Reports...'
            
            // 1. Archive ONLY the new reports (ignore old ones if any)
            archiveArtifacts artifacts: 'reports/ReqRes_API_ExtentReport.html, reports/UI_ExtentReport.html', fingerprint: true
            
            // 2. Publish Dashboard
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                // Explicitly list ONLY the files we just generated
                reportFiles: 'ReqRes_API_ExtentReport.html, UI_ExtentReport.html',
                reportName: 'Automation Reports',
                reportTitles: 'ReqRes API Results, UI Results'
            ])
        }
    }
}