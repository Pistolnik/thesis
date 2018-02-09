import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def env = System.getenv()
def adminName = env.get('JENKINS_ADMIN_NAME')
def adminPassword = env.get('JENKINS_ADMIN_PASSWORD')
def developerName = env.get('JENKINS_DEVELOPER_NAME')
def developerPassword = env.get('JENKINS_DEVELOPER_PASSWORD')
def hudsonRealm = new HudsonPrivateSecurityRealm(false)

// create users
hudsonRealm.createAccount(adminName, adminPassword)
hudsonRealm.createAccount(developerName, developerPassword)
instance.setSecurityRealm(hudsonRealm)
instance.save()

// give administrator rights to the users
def strategy = new GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER, adminName)
strategy.add(Jenkins.ADMINISTER, developerName)
instance.setAuthorizationStrategy(strategy)
instance.save()
