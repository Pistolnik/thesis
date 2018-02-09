import jenkins.model.*

def mavenPluginExtension = Jenkins.instance.getExtensionList(hudson.tasks.Maven.DescriptorImpl.class)[0]
def asList = (mavenPluginExtension.installations as List)

asList.add(new hudson.tasks.Maven.MavenInstallation('Maven 3.3.9', null, [new hudson.tools.InstallSourceProperty([new hudson.tasks.Maven.MavenInstaller("3.3.9")])]))
mavenPluginExtension.installations = asList
mavenPluginExtension.save()


// script inspired by https://github.com/batmat/jez/blob/master/jenkins-master/init_scripts/add_maven_auto_installer.groovy
