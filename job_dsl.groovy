folder('Tools') {
    displayName('Tools')
    descriptionr('Folder for miscellanous tools.')

    job('Tools/clone-repository') {
        parameters {
            string(name: 'GIT_REPOSITORY_URL', description: 'Git URL of the repository to clone')
        }
        steps {
            cleanWs()
            sh("git clone ${params.GIT_REPOSITORY_URL}")
        }
    }

    job('Tools/SEED') {*
        parameters {
            string(name: 'GITHUB_NAME', description: 'GitHub repository owner/repo_name (e.g.: "EpitechIT31000/chocolatine")')
            string(name: 'DISPLAY_NAME', description: 'Display name for the job')
        }
        steps {
            dsl {
                text('''
                job(name) {
                    displayName(params.DISPLAY_NAME)
                    description('Generated job by Seed job DSL script')
                    scm {
                        git {
                            remote {
                                url("https://github.com/${params.GITHUB_NAME}.git")
                            }
                        }
                    }
                    steps {
                        cleanWs()
                        sh("make fclean")
                        sh("make")
                        sh("make tests_run")
                        sh("make clean")
                    }
                    triggers {
                        pollSCM('*/1 * * * *')
                    }
                }
                ''')
            }
        }
    }
}