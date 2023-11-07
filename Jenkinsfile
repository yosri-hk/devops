pipeline {
    agent {label 'ubuntu_agent'}

    stages {
        stage('Clone') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'yosri-hk', credentialsId: 'git', url: 'https://github.com/rimbsaad/DevopsProject.git'
            }
        }
        
        stage('Build') {
            steps {
                sh "mvn clean package -Ptest"
                //sh "mvn clean package -DskipTests"
            }

            post {
                success {
                    // If Maven was able to run the tests, even if some of the tests failed,
                    // record the test results and archive the jar file.
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage('Test'){
            steps {
                sh "mvn test -Ptest"
            }
        }
    
        stage('Run') {
            steps {
                sh "docker build -t ski_app ."
                sh "docker-compose up -d"
            }
        }
        stage('UploadArtifact'){
            steps{
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '192.168.1.19:9082',
                    groupId: 'tn.esprit.ds',
                    version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                    repository: 'ski-repo',
                    credentialsId: 'nexus_login',
                    artifacts: [
                        [artifactId: 'SkiStationProject',
                        classifier: '',
                        file:  'target/SkiStationProject-0.0.1-SNAPSHOT.jar',
                            type: 'jar']
                    ]
     )
            }
            
        }
        stage('Checkstyle Analysis'){
            steps {
                sh 'mvn checkstyle:checkstyle'
                
            }
        }

        stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'sonar4.7'
            }
            steps {
               withSonarQubeEnv('sonar') {
                   sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ski \
                   -Dsonar.projectName=ski \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.java.binaries=target/test-classes/tn/esprit/SkiStationProject/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
                   
              }
            }
        }
         stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        }
    }
