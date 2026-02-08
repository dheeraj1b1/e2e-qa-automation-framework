pipeline {
    agent any

    environment {
        // Required for UI tests to run on Linux (Headless Chrome)
        HEADLESS = 'true'
    }

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

        stage('Run ReqRes API Tests') {
            steps {
                echo 'Starting ReqRes API Test Suite (CI-Safe)...'
                // Runs the new AWS-safe API tests. 
                // Uses 'clean' to wipe previous results.
                sh './gradlew clean test -Psuite=api-ci'
            }
        }

        stage('Run UI Tests') {
            steps {
                echo 'Starting UI Test Suite...'
                // Runs UI tests. Note: We do NOT use 'clean' here 
                // so we don't accidentally delete the API report generated above.
                sh './gradlew test -Psuite=ui'
            }
        }
    }

    post {
        always {
            echo 'Archiving Extent Reports...'
            
            // Archives all HTML files in the reports folder
            archiveArtifacts artifacts: 'reports/*.html', fingerprint: true
            
            // Publishes the dashboard with the NEW report names
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                // Make sure these match the filenames your framework generates!
                reportFiles: 'ReqRes_API_ExtentReport.html, UI_ExtentReport.html',
                reportName: 'Automation Reports',
                reportTitles: 'ReqRes API Results, UI Results'
            ])
        }
    }
}