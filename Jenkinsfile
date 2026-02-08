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
            
            // 1. Archive the raw files so they are downloadable
            archiveArtifacts artifacts: 'reports/*.html', fingerprint: true
            
            // 2. Publish API Report (Tab 1)
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ReqRes_API_ExtentReport.html', // Specific File
                reportName: 'API_Test_Results',              // Unique Name for Sidebar
                reportTitles: 'ReqRes API Execution Report'
            ])

            // 3. Publish UI Report (Tab 2)
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'UI_ExtentReport.html',         // Specific File
                reportName: 'UI_Test_Results',               // Unique Name for Sidebar
                reportTitles: 'UI Automation Execution Report'
            ])
        }
    }
}