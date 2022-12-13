def sonarcheck() {
    sh ''' 
        sonar-scanner -Dsonar.host.url=http://172.31.29.248:9000 -Dsonar.projectkey=${Component} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
        sonar-quality-gate ${SONAR_USR} ${SONAR_PSW} 172.31.29.248 ${Component}   // This give the result of the scan and based on that it will abort the pipeline or will proceed further
        echo sonarchecks for ${Component}
    '''
}
