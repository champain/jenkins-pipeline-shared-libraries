//import hudson.model.Result
import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper


def call(RunWrapper currentBuild, List<String> emailList) {
    def currentResult = currentBuild.currentResult
    def previousResult = currentBuild.getPreviousBuild()?.getResult()

    def buildFixed = (
        (currentResult == Result.SUCCESS) &&
        (currentResult != previousResult) &&
        (previousResult != null)
    )

    def badResult = (
        currentResult == Result.FAILURE || currentResult == Result.UNSTABLE
    )

    println("currentResult: ${currentResult}")
    println("previousResult: ${previousResult}")
    println("badResult: ${badResult}")
    println("buildFixed: ${buildFixed}")
    println("constants: failure - ${Result.FAILURE} - unstable ${Result.UNSTABLE}")

    if (buildFixed || badResult) {
        emailext (
            recipientProviders: [[$class: "RequesterRecipientProvider"]],
            to: emailList.join(", "),
            subject: 'test subject', // subject: "\$DEFAULT_SUBJECT",
            body: 'test body' //body: "\$DEFAULT_BODY"
        )
    }
}
