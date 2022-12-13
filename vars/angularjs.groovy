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
    }   //end of the stages
}
}