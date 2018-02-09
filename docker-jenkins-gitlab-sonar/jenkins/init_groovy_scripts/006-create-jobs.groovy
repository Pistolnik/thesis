import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement

def mergeRequestJob = '''
job('Test Merge Request') {
    scm {
      git{
        remote {
            name('origin')
            url('git@gitlab.dev:jenkins/test.git')
            credentials('jenkins_at_gitlab_key')
            branches('origin/\${gitlabSourceBranch}')
        }
      }
    }
    steps {
        maven {
            goals('--batch-mode verify sonar:sonar -Dsonar.host.url=http://sonar.dev:9000 -Dsonar.analysis.mode=preview -Dsonar.gitlab.commit_sha=\\\${gitlabMergeRequestLastCommit} -Dsonar.gitlab.ref_name=feature -Dsonar.gitlab.project_id=jenkins/test -Dsonar.gitlab.url=http://gitlab.dev -Dsonar.gitlab.user_token=\\\${JENKINS_SECRET_TOKEN}')
            mavenOpts('-Xms256m')
            mavenOpts('-Xmx512m')
            properties(skipTests: false)
            mavenInstallation('Maven 3.3.9')
        }
    }
    triggers {
        gitlabPush {
            buildOnMergeRequestEvents(true)
            buildOnPushEvents(false)
            enableCiSkip(false)
            setBuildDescription(true)
            addNoteOnMergeRequest(false)
            rebuildOpenMergeRequest('source')
            addVoteOnMergeRequest(false)
            useCiFeatures(false)
            acceptMergeRequestOnSuccess(false)
        }
    }
    wrappers {
        credentialsBinding {
            usernamePassword('JENKINS_USERNAME', 'JENKINS_SECRET_TOKEN', 'jenkins_token')
        }
    }
    publishers {
        archiveJunit('**/target/surefire-reports/*.xml')
    }
}
'''

def mergeToMasterJob = '''
job("Merge to Master") {
    scm {
      git{
        remote {
            name('origin')
            url('git@gitlab.dev:jenkins/test.git')
            credentials('jenkins_at_gitlab_key')
            branches('origin/\${gitlabSourceBranch}')
        }
      }
    }
    steps {
        maven {
            goals('--batch-mode verify sonar:sonar -Dsonar.host.url=http://sonar.dev:9000 -Dsonar.analysis.mode=publish')
            mavenOpts('-Xms256m')
            mavenOpts('-Xmx512m')
            properties(skipTests: false)
            mavenInstallation('Maven 3.3.9')
        }
    }
    triggers {
        gitlabPush {
            buildOnMergeRequestEvents(false)
            buildOnPushEvents(true)
            setBuildDescription(true)
            addNoteOnMergeRequest(false)
            rebuildOpenMergeRequest('never')
            addVoteOnMergeRequest(false)
            acceptMergeRequestOnSuccess(false)
            targetBranchRegex('master')
        }
    }
    wrappers {
        credentialsBinding {
            usernamePassword('JENKINS_USERNAME', 'JENKINS_SECRET_TOKEN', 'jenkins_token')
        }
    }
    publishers {
        archiveJunit('**/target/surefire-reports/*.xml')
    }
}
'''
def workspace = new File('.')

def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)

new DslScriptLoader(jobManagement).runScript(mergeRequestJob)

new DslScriptLoader(jobManagement).runScript(mergeToMasterJob)




// inspired from https://devops.datenkollektiv.de/from-plain-groovy-to-jenkins-job-dsl-a-quantum-jump.html
