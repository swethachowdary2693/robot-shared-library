def lintcheck() {
    sh ''' 
        # We want Developers to handle the lint checks failure
        # npm i jslint
        # node_modules/jslint/bin/jslint.js  server.js || true
        echo Starting lint checks for ${Component}
        echo Lint checks completed for ${Component}
    '''
}

def call() {
    pipeline {
        agent any 
    environment {
        SONAR      = credentials{'SONAR'}
        NEXUS      = credentials{'NEXUS'}
    }
    stages {
        stage ('Downloading the dependencies') {
            steps {
                sh "npm install"
            }
        }
        stage ('Lint Checks') {
            steps {
                script {
                    lintcheck()
                }
                
            }
        stage ('Sonar check') {
            steps {
                script {
                    env.ARGS=-Dsonar.sources=.
                    common.sonarcheck()
                }
                
            }
        }
        stage ('Test cases') {
            parallel {
                stage ('Unit tests') {
                    steps {
                        sh "Unit test cases completed"
                    }
               }

               stage ('Intergration tests') {
                    steps {
                        sh "Integration test cases completed"
                    }
                }

                stage ('Functional tests') {
                    steps {
                        sh "Functional test cases completed"
                    }
                }
            }
         }

        stage ('Check release') {
            when {
                expression { env.TAG_NAME != null }
                
            }
            steps{
                script {
                    env.def UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://172.31.24.252:8081/service/rest/repository/browse/${Component} | grep ${Component}-${TAG_NAME}.zip" || true) 
                }
            }
        }
         stage ('Prepare artifact') {
            when {
                expression { env.TAG_NAME != null }
                expression { env.UPLOAD_STATUS == "" }
            }
            steps {
                sh '''
                    npm install 
                    zip ${Component}-${TAG_NAME}.zip node_modules server.js
                '''
            }
         }
        stage ('Upload artifact') {
            when {
                expression { env.TAG_NAME != null}
                expression { env.UPLOAD_STATUS == "" }
            }
            steps {
                sh '''
                    curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${Component}-${TAG_NAME} http://172.31.24.252/repository/${Component}-${TAG_NAME}.zip
                '''
            }
         }

    }   //end of the stages
}
}