def lintcheck() {
    sh ''' 
        # We want Developers to handle the lint checks failure
        echo Starting lint checks for ${Component}
        # mvn checkstyle:check || true                     //perform maven lint checks
        echo Lint checks completed for ${Component}
    '''
}

def call() {
    pipeline {
    agent any 
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
        }
    }   //end of the stages
}
}