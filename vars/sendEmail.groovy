def call(currentBuild, List emailList) {
    """
    def curBuild = currentBuild.currentResult
    def prevBuild = currentBuild.getPreviousBuild()?.getResult() ?: null
    def buildFixed = curBuild == Result.SUCCESS && prevBuild != curBuild && prevBuild != null
    def sendEmail = (curBuild in [Result.FAILURE, Result.UNSTABLE]) || buildFixed
    """
    emailext (
        recipientProviders: [[$class: "RequesterRecipientProvider"]],
        to: emailList.join(", "),
        subject: 'test subject', // subject: "\$DEFAULT_SUBJECT",
        body: 'test body' //body: "\$DEFAULT_BODY"
    )
}
