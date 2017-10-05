# Notes on deployment

## Working 

```
@Library('github.com/champain/jenkins-automation')

import gov.cfpb.PipelineUtils
import hudson.model.Result


def emailScript = {
    def curBuild = currentBuild.currentResult
    def prevBuild = currentBuild.getPreviousBuild()?.getResult() ?: null
    def buildStatusChanged = ( 
        curBuild == Result.SUCCESS && prevBuild != curBuild && prevBuild
    )
    def sendEmail = (curBuild in [Result.FAILURE, Result.UNSTABLE ]) || buildStatusChanged
    
    if (sendEmail) {
        emailext (
            recipientProviders: [[$class: "RequesterRecipientProvider"]],
            to: "foo@bar",
            subject: "\$DEFAULT_SUBJECT",
            body: "\$DEFAULT_BODY"
        )
    }
}

pipeline {
    agent { label "master" }

    stages {
        stage("Restart all services") {
            steps {
                echo "foo"
            }
        }
    }
    post {
        always {
            script {
                    emailScript()
            }
        }
    }
}
```

