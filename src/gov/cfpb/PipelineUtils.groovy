package gov.cfpb

import hudson.model.Result
import java.io.File


/**
 * Pipeline utils class to house reusable Pipeline code
 */
class PipelineUtils {

    /** Utility function to add extended email
     *
     * @param currentBuild TODO XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     * @param List emailList List of email addresses
     */

    static void email(currentBuild, List emailList) {
        """
        def curBuild = currentBuild.currentResult
        def prevBuild = currentBuild.getPreviousBuild()?.getResult() ?: null
        def buildFixed = curBuild == Result.SUCCESS && prevBuild != curBuild && prevBuild != null
        def sendEmail = (curBuild in [Result.FAILURE, Result.UNSTABLE]) || buildFixed
        """
        def sendEmail = true
        if (sendEmail && emailList) {
            return emailext (
                recipientProviders: [[$class: "RequesterRecipientProvider"]],
                to: emailList.join(", "),
                subject: 'test subject', // subject: "\$DEFAULT_SUBJECT",
                body: 'test body' //body: "\$DEFAULT_BODY"
            )
        }
    }

}
