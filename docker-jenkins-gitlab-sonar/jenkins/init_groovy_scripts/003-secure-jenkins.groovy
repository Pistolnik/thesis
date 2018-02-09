import jenkins.model.GlobalConfiguration
import jenkins.model.Jenkins
import jenkins.security.s2m.AdminWhitelistRule
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration


// disable jenkins cli
jenkins.model.Jenkins.instance.getDescriptor("jenkins.CLI").get().setEnabled(false)

// enable slave to master access control: https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
