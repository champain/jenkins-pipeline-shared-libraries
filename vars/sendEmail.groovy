import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper


def call(RunWrapper currentBuild, List<String> emailList) {
    // We are using Result.toString() instead of doing a less-stringly-typed
    // approach with Result.fromString() (combined with the Result comparison
    // methods) because Result.fromString() is not available when pipeline
    // jobs are sandboxed. We'd like for this shared library to be usable even
    // in sandboxed mode. For more info, see:
    // https://wiki.jenkins.io/display/JENKINS/Script+Security+Plugin/#ScriptSecurityPlugin-User%E2%80%99sguide

    def currentResult = currentBuild.currentResult
    def previousResult = currentBuild.getPreviousBuild()?.getResult()

    def buildFixed = (
        (currentResult == Result.SUCCESS.toString()) &&
        (currentResult != previousResult) &&
        (previousResult != null)
    )

    def badResult = (
        currentResult in [Result.UNSTABLE.toString(), Result.FAILURE.toString()]
    )

    if (buildFixed || badResult) {
        emailext (
            recipientProviders: [[$class: "RequesterRecipientProvider"]],
            to: emailList.join(", "),
            subject: "\$DEFAULT_SUBJECT",
            body: "\$DEFAULT_BODY"
        )
    }
}
