package gov.cfpb

import hudson.model.Result

/**
 * Pipeline utils class to house reusable Pipeline code
 */
class PipelineUtils {

    /** Utility function to add extended email
     *
     * @param List emailList List of email addresses
     */

    static void emailPipelineNotify(currentBuild, 
                                    List emailList = ["foo@bar"]) 
    { 
        def curBuild = currentBuild.currentResult
        def prevBuild = currentBuild.getPreviousBuild()?.getResult() ?: null
        def buildStatusChanged = ( 
            curBuild == Result.SUCCESS && prevBuild != curBuild && prevBuild
        )
        def sendEmail = (curBuild in [Result.FAILURE, Result.UNSTABLE ]) || buildStatusChanged 

        if (sendEmail) {
            emailext (
                recipientProviders: [[$class: "RequesterRecipientProvider"]],
                to: emailList().join(", "),
                subject: "\$DEFAULT_SUBJECT",
                body: "\$DEFAULT_BODY"
            )
        }
    }
}

