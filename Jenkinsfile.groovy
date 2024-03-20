/* groovylint-disable CompileStatic, DuplicateMapLiteral, DuplicateStringLiteral, Indentation, LineLength, NestedBlockDepth, NglParseError, NoDef, ThrowException, TrailingWhitespace, VariableTypeRequired */

/* groovylint-disable-next-line NglParseError */
pipeline 
{
    agent any

    stages {
        stage('Clone from GitHub') {
            steps {
               script {
                    try {
                        withCredentials([usernamePassword(credentialsId: 'Github_UPT', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                            def githubUrl = 'https://' + GITHUB_TOKEN + '@github.com/' + GITHUB_USERNAME + '/Jenkins_CI_Project.git'
                            def branchName = 'main' // Replace with your desired branch
                            checkout([$class: 'GitSCM',
                                branches: [[name: branchName]],
                                userRemoteConfigs: [[
                                    url: githubUrl
                                ]]
                            ])
                        }
                    } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error occurred while cloning code from GitHub: ${e}")
                    }
               }
            }
        }
        stage('Installing dependencies') {
            steps {
               script {
                    try {
                    dir('NodeJS_Project')
                    {
                      sh'''
                      npm ci
                      '''     
                    }
                } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error occurred installing dependencies: ${e}")
                    }
               }
            }
        }
        stage('Linting') {
            steps {
               script {
                    try {
                    dir('NodeJS_Project')
                    {
                      sh'''
                      npm run lint
                      '''     
                    }
                } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error occurred while performing lint: ${e}")
                    }
               }
            }
        }
        
        stage('Unit testing') {
            steps {
               script {
                    try {
                    dir('NodeJS_Project')
                    {
                      sh'''
                      npm run test-ci
                      '''     
                    }
                } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error occurred while performing unit tests: ${e}")
                    }
               }
            }
        }

        stage('Coverage') {
            steps {
               script {
                    try {
                    dir('NodeJS_Project')
                    {
                      sh'''
                      npm run coverage
                      '''     
                    }
                } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error occurred while generating coverage report: ${e}")
                    }
               }
            }
        }
        stage('Sonarqube') {
             environment {
            SCANNER_HOME = tool 'SonarQubeScanner'
             }
            steps {
               script {
                    try {
                         dir('NodeJS_Project') {
                    withSonarQubeEnv('sonarqube') {
                                sh '$SCANNER_HOME/bin/sonar-scanner'
                    }

                                timeout(time: 5, unit: 'MINUTES') {
                                    def qualitygate = waitForQualityGate()
                                    if (qualitygate.status != 'OK') {
                                        throw new Exception('The quality gate did not succeed. Please check the sonar dashboard')
                                    }
                                }
                                currentBuild.result = 'SUCCESS'
                         }               
                } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error in sonarqube phase: ${e}")
                    }
               }
            }
        }

          stage('Docker image build and push') {
            steps {
               script {
                    try {
                    dir('NodeJS_Project')
                    {
                      withCredentials([usernamePassword(credentialsId: 'DockerHubUPT', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                         def commitId = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                          /* groovylint-disable-next-line GStringExpressionWithinString */
                        sh '''
                        echo ''' + DOCKER_PASSWORD + ''' | docker login -u ''' + DOCKER_USERNAME + ''' --password-stdin
                        docker build -t kshitij98/jenkins_ci:''' + commitId + ''' .
                        docker push kshitij98/jenkins_ci:''' + commitId + '''
                        '''
                      }
                    }
                } catch (e) {
                        currentBuild.result = 'FAILURE'
                        error("Error in docker image generation and pushing: ${e}")
                    }
               }
            }
        }
    }
    
      post {
        success {
            echo 'The Pipeline has succeeded!'
            sh '''
            docker rmi $(docker images -q kshitij98/jenkins_ci)
            '''
            cleanWs()
        }

        failure {
            echo 'The Pipeline has failed!'
            sh '''
            docker rmi $(docker images -q kshitij98/jenkins_ci)
            '''
           cleanWs()
        }
      }
}

