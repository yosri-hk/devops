pipeline {
    
    agent none
    environment {
        registryCredential = 'nexus_login'
        appRegistry = "http://192.168.1.19:9082/repository/ski_app_docker_image/"
        
        
        
    }
    stages {
        stage('USER Infos'){
            agent any
            steps {
               script {
                    wrap([$class: 'BuildUser']) {
                        echo "BUILD_USER=${BUILD_USER}"
                        echo "BUILD_USER_EMAIL=${BUILD_USER_EMAIL}"
                      
                    }
               }
           }
        }
        
        stage('Clone') {
            agent {label 'ubuntu_agent'}
            steps {
                // Get some code from a GitHub repository
                git branch: 'yosri-hk', credentialsId: 'git', url: 'https://github.com/rimbsaad/DevopsProject.git'
            }
        }
        
        stage('Build') {
            agent {label 'ubuntu_agent'}
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
            agent {label 'ubuntu_agent'}
            steps {
                sh "mvn test -Ptest"
            }
        }
    
       
        

        stage('UploadArtifact to nexus'){
            agent {label 'ubuntu_agent'}
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
       
         stage('Run') {
            agent {label 'ubuntu_agent'}
            steps {
                sh "docker build -t ski_app ."
                sh "docker-compose up -d"
            }
        }
        stage('upload docker image to nexus') {
            agent {label 'ubuntu_agent'}
            steps {
                sh "docker login -u admin -p 26198240 192.168.1.19:8083"
                sh "docker tag ski_app:latest 192.168.1.19:8083/repository/ski_app_docker_image/ski_app:latest-${env.BUILD_ID}"
                sh "docker push 192.168.1.19:8083/repository/ski_app_docker_image/ski_app:latest-${env.BUILD_ID}"
            }
        }
        

        
        stage('Checkstyle Analysis'){
            agent {label 'ubuntu_agent'}
            steps {
                sh 'mvn checkstyle:checkstyle'
                
                
            }
        }
        stage('Sonar Analysiss') {
            agent {label 'ubuntu_agent'}
    steps {
        // Set up SonarQube environment for server named 'sonar'
        withSonarQubeEnv('sonar') {
            // Run SonarQube analysis using 'mvn sonar:sonar'
            sh 'mvn verify sonar:sonar'
        }
    }
}

        
         stage("Quality Gate") {
             agent {label 'ubuntu_agent'}
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
            }
           
    
     post {
                always {
                    script {
                        def subject = "Pipeline Status: ${currentBuild.currentResult}"
                        def body = """
                                    Pipeline Status: ${currentBuild.currentResult}
                                    Job Name: ${env.JOB_NAME}
                                    Build Number: ${env.BUILD_NUMBER}
                                    Build URL: ${env.BUILD_URL}
                                    """

                 mail(
                    bcc: '',
                    body: body,
                    cc: '',
                    from: '',
                    replyTo: '',
                    subject: subject,
                    to: """${env.BUILD_USER_EMAIL}"""
                )
                
         
            }
            
        }
        

        }
    }      }
      
}
