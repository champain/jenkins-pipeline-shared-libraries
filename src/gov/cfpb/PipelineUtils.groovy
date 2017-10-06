package gov.cfpb

import hudson.model.Result


def foo() {
    println('foo')
}



/**
 * Pipeline utils class to house reusable Pipeline code
 */
class PipelineUtils {

    /** Utility function to add extended email
     *
     * @param List emailList List of email addresses
     */

    static void email(currentBuild, List emailList) {
        def curBuild = currentBuild.currentResult
        def prevBuild = currentBuild.getPreviousBuild()?.getResult() ?: null
        def buildFixed = curBuild == Result.SUCCESS && prevBuild != curBuild && prevBuild
        def sendEmail = (curBuild in [Result.FAILURE, Result.UNSTABLE]) || buildFixed

        if (sendEmail && emailList) {
            emailext (
                recipientProviders: [[$class: "RequesterRecipientProvider"]],
                to: emailList().join(", "),
                subject: "\$DEFAULT_SUBJECT",
                body: "\$DEFAULT_BODY"
            )
        }
    }

}
