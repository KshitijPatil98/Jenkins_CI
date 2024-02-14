/* groovylint-disable CompileStatic, DuplicateMapLiteral, DuplicateStringLiteral, Indentation, LineLength, NestedBlockDepth, NglParseError, NoDef, ThrowException, VariableTypeRequired */

pipeline {
    agent any

    stages {
        stage('Clone the code from GitLab') {
            steps {
                echo 'Notify GitLab'
                updateGitlabCommitStatus name: 'Clone Repo', state: 'pending'

                script {
                    try {
                        withCredentials([usernamePassword(credentialsId: 'Gitlab_Incalus_UP', usernameVariable: 'GITLAB_USERNAME', passwordVariable: 'GITLAB_PASSWORD')]) {
                            def gitlabUrl = 'https://' + GITLAB_USERNAME + ':' + GITLAB_PASSWORD + '@gitlab.com/incalus-edi/edi-chaincode.git'
                            def branchName = ''

                            if (env.gitlabMergeRequestState == 'opened') {
                                branchName = env.gitlabBranch
                            } else if (env.gitlabMergeRequestState == 'merged') {
                                branchName = env.gitlabTargetBranch
                            } else {
                                throw new Exception("Please make sure its a merge request. The gitlabMergeRequestState variable is not set : ${gitlabMergeRequestState}")
                            }

                            checkout([$class: 'GitSCM',
                                branches: [[name: branchName]],
                                userRemoteConfigs: [[
                                    url: gitlabUrl
                                ]]
                            ])
                        }
                        updateGitlabCommitStatus name: 'Clone Repo', state: 'success'
                    } catch (e) {
                        echo "Error occurred while cloning code from the repo: ${e}"
                        updateGitlabCommitStatus name: 'Clone Repo', state: 'failed'
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }

        stage('MR into develop') {
            environment {
                SCANNER_HOME = tool 'SonarQubeScanner'
            }

            when {
                expression {
                    env.gitlabMergeRequestState == 'opened' && env.gitlabTargetBranch == 'develop'
                }
            }

            steps {
                script {
                    def dep = false
                    def lint = false
                    def ut = false
                    def coverage = false
                    def imageTest = false
                    def sonar = false

                    try {
                        dir('edi') {
                            withCredentials([usernamePassword(credentialsId: 'Gitlab_Incalus_UP', usernameVariable: 'GITLAB_USERNAME', passwordVariable: 'GITLAB_PASSWORD')]) {
                                updateGitlabCommitStatus name: 'Download Dependencies', state: 'pending'
                                sh 'npm ci'
                                dep = true
                                updateGitlabCommitStatus name: 'Download Dependencies', state: 'success'

                                updateGitlabCommitStatus name: 'Linting', state: 'pending'
                                sh 'npm run lint'
                                lint = true
                                updateGitlabCommitStatus name: 'Linting', state: 'success'

                                updateGitlabCommitStatus name: 'Unit test', state: 'pending'
                                sh 'npm run test-ci'
                                ut = true
                                updateGitlabCommitStatus name: 'Unit test', state: 'success'

                                updateGitlabCommitStatus name: 'Coverage', state: 'pending'
                                sh 'npm run coverage'
                                coverage = true
                                updateGitlabCommitStatus name: 'Coverage', state: 'success'

                                updateGitlabCommitStatus name: 'Docker Image Sanity Check', state: 'pending'
                                sh '''
                                docker build -t  edi_cc_test .
                                sleep 2
                                if docker images -q edi_cc_test >/dev/null; then
                                   echo "Docker image exists"
                                else
                                   echo "Docker image does not exist"
                                   exit 1
                                fi
                                '''
                                imageTest = true
                                updateGitlabCommitStatus name: 'Docker Image Sanity Check', state: 'success'

                                updateGitlabCommitStatus name: 'Sonar', state: 'pending'
                                withSonarQubeEnv('sonarqube') {
                                    sh '$SCANNER_HOME/bin/sonar-scanner'
                                }

                                timeout(time: 5, unit: 'MINUTES') {
                                    def qualitygate = waitForQualityGate()
                                    if (qualitygate.status != 'OK') {
                                        throw new Exception('The quality gate did not succeed. Please check the sonar dashboard')
                                    }
                                }

                                sonar = true
                                updateGitlabCommitStatus name: 'Sonar', state: 'success'
                                currentBuild.result = 'SUCCESS'
                            }
                        }
                    } catch (e) {
                        echo 'Inside catch block for MR into develop stage'
                        echo "The following error has occurred: ${e}"

                        if (!dep) {
                            updateGitlabCommitStatus name: 'Downloading Dependencies', state: 'failed'
                        } else if (!lint) {
                            updateGitlabCommitStatus name: 'Linting', state: 'failed'
                        } else if (!ut) {
                            updateGitlabCommitStatus name: 'Unit test', state: 'failed'
                        } else if (!coverage) {
                            updateGitlabCommitStatus name: 'Coverage', state: 'failed'
                        } else if (!imageTest) {
                            updateGitlabCommitStatus name: 'Docker Image Sanity Check', state: 'failed'
                        } else if (!sonar) {
                            updateGitlabCommitStatus name: 'Sonar', state: 'failed'
                        }

                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }

        stage('MR/PUSH into develop ') {
            when {
                expression {
                    (env.gitlabMergeRequestState == 'merged' && env.gitlabTargetBranch == 'develop')
                }
            }

            steps {
                echo 'Notify GitLab'
                updateGitlabCommitStatus name: 'merge', state: 'pending'

                script {
                    try {
                        sh '''
                        echo "This is a merge/push into develop branch"
                        '''
                        updateGitlabCommitStatus name: 'merge', state: 'success'
                    } catch (e) {
                        echo 'Inside catch'
                        echo "Caught: ${e}"
                        updateGitlabCommitStatus name: 'merge', state: 'failed'
                    }
                }
            }
        }

        stage('Push into release') {
            when {
                expression {
                    env.gitlabSourceBranch.startsWith('release/') && env.gitlabTargetBranch.startsWith('release/')
                }
            }

            steps {
                updateGitlabCommitStatus name: 'test', state: 'pending'

                script {
                    sh '''
                    echo "This is a push into release"
                    '''
                }

                updateGitlabCommitStatus name: 'test', state: 'success'
            }
        }
    }

    post {
        success {
            echo 'The Pipeline has succeeded!'
            sh '''
             rm -rf *
             docker rmi edi_cc_test
             '''
        }

        failure {
            echo 'The Pipeline has failed!'
            sh 'rm -rf *'
        }
    }
}
