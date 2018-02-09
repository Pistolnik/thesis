import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

def domain = Domain.global()
def store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
// load private key from local storage
def privateKey = new BasicSSHUserPrivateKey(
                     CredentialsScope.GLOBAL,
                     "jenkins_at_gitlab_key",                                   // id
                     "jenkins",                                                 // username
                     new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource("/usr/share/jenkins/ref/jenkins_at_gitlab.key"),
                     "",                                                        // password
                     "Jenkins private key to jenkins account at gitlab"         // description
)
// save the key as credentials
store.addCredentials(domain, privateKey)


// script inspired by https://stackoverflow.com/questions/41870688/automatically-setup-a-jenkins-2-32-1-server-with-a-script
