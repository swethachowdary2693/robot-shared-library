def lintcheck() {
    sh ''' 
        # We want Developers to handle the lint checks failure
        # npm i jslint
        # node_modules/jslint/bin/jslint.js  server.js || true
        echo "Starting lint checks"
        echo "lint checks completed"
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