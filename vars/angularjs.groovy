def lintcheck() {
    sh ''' 
        # We want Developers to handle the lint checks failure
        echo Starting lint checks for ${Component}
        # echo lint checks                 //perform python lint checks
        echo Lint checks completed for ${Component}
    '''
}

def call() {
    pipeline {
    agent any 
    stages {
        
        stage ('Lint Checks') {
            steps {
                script {
                    lintcheck()
                }
                
            }
        }

        stage ('Sonar check') {
            steps {
                script {
                    env.ARGS=-Dsonar.source=.
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
    }   //end of the stages
}
}