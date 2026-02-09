pipeline {
    agent any

    environment {
        HEADLESS = 'true'
        // Inject Secrets
        DB_PASSWORD = credentials('DB_PASSWORD_ID') 
        // Use 'localhost' because Jenkins runs ON the DB server
        DB_URL = 'jdbc:mysql://localhost:3306/automation_db'
        DB_USER = 'qa_user'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Clean Workspace') {
            steps {
                sh 'rm -rf reports/*' 
                sh 'chmod +x ./gradlew'
            }
        }

        // 1. Run ReqRes API Tests
        stage('Run API Tests') {
            steps {
                echo 'Running API Suite...'
                sh './gradlew clean test -Psuite=api-ci' 
            }
        }

        // 2. Run UI Tests
        stage('Run UI Tests') {
            steps {
                echo 'Running UI Suite...'
                sh './gradlew test -Psuite=ui' 
            }
        }

        // 3. Run DB Tests (NEW)
        stage('Run DB Validation') {
            steps {
                echo 'Running Database Validation Suite...'
                // Validates the data "mocked" or created by previous steps
                sh './gradlew test -Psuite=db' 
            }
        }
    }

    post {
        always {
            echo 'Archiving Reports...'
            archiveArtifacts artifacts: 'reports/*.html', fingerprint: true
            
            // PUBLISH 3 TABS
            publishHTML(target: [
                allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true,
                reportDir: 'reports', reportFiles: 'ReqRes_API_ExtentReport.html',
                reportName: 'API_Results', reportTitles: 'API Report'
            ])
            
            publishHTML(target: [
                allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true,
                reportDir: 'reports', reportFiles: 'UI_ExtentReport.html',
                reportName: 'UI_Results', reportTitles: 'UI Report'
            ])

            publishHTML(target: [
                allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true,
                reportDir: 'reports', reportFiles: 'DB_Validation_ExtentReport.html',
                reportName: 'DB_Results', reportTitles: 'Database Report'
            ])
        }
    }
}