pipeline {
    agent any

    environment {
        // This tells your UI tests to run without opening a visible browser window
        // (Required for AWS Linux servers)
        HEADLESS = 'true' 
    }

    tools {
        // If you configured a specific Gradle version in Jenkins tools, reference it here.
        // Otherwise, we will use the wrapper ./gradlew
        jdk 'Default' 
    }

    stages {
        stage('Checkout') {
            steps {
                // Pulls the latest code from your GitHub repo
                checkout scm
            }
        }

        stage('Setup Permissions') {
            steps {
                // Ensures the Gradle wrapper is executable on Linux
                sh 'chmod +x ./gradlew'
            }
        }

        stage('Run API Tests') {
            steps {
                echo 'Starting API Test Suite...'
                // Runs only the API suite defined in your build.gradle
                sh './gradlew clean test -Psuite=api'
            }
        }

        stage('Run UI Tests') {
            steps {
                echo 'Starting UI Test Suite...'
                // Runs the UI suite. The HEADLESS=true env var (defined above) 
                // should be read by your DriverManager class.
                sh './gradlew test -Psuite=ui'
            }
        }
    }

    post {
        always {
            echo 'Archiving Extent Reports...'
            
            // 1. Archive the HTML files so you can download them
            archiveArtifacts artifacts: 'reports/*.html', fingerprint: true
            
            // 2. Publish them to the Jenkins Dashboard (needs HTML Publisher Plugin)
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