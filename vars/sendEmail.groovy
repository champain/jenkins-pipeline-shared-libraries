import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper


def call(RunWrapper currentBuild, List<String> emailList) {
    def currentResult = Result.fromString(currentBuild.currentResult)
    def previousResult = Result.fromString(currentBuild.getPreviousBuild()?.getResult())


    def buildFixed = ( 
        currentResult.isBetterOrEqualTo(Result.SUCCESS) &&
        previousResult.isWorseThan(Result.SUCCESS)
    )

    def badResult = (
        currentResult.isWorseThan(Result.SUCCESS) &&
        currentResult.isBetterThan(Result.NOT_BUILT)
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

