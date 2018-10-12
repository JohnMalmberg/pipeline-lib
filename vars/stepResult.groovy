// vars/stepResult.groovy
def call(Map config) {

    node {
        currentBuild.result = config.get('result')

        if (env.CHANGE_ID) {
           if (config['result'] == "ABORTED" ||
               config['result'] == "UNSTABLE" ||
               config['result'] == "FAILURE") {
                pullRequest.comment("Test stage ${config.name}" +
                                    " completed with status " +
                                    "${config.result}" +
                                    ".  " + env.BUILD_URL +
                                    "display/redirect")
            }

            switch(config['result']) {
                case "UNSTABLE":
                    result = "FAILURE"
                    break
                case "FAILURE":
                    result = "ERROR"
                    break
            }
            /* java.lang.IllegalArgumentException: The supplied credentials are invalid to login
             * probably due to changing the account from me to daos-jenkins
             * might need to just recreate the org with the daos-jenkins account
            githubNotify description: ${config.name}, context:
                                      ${config.context} + "/" +
                                      ${config.name},
                                      status: result
            */
        }
    }
}
