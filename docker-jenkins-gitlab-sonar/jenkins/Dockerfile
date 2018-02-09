FROM jenkins:2.60.2

COPY jenkins_at_gitlab.key /usr/share/jenkins/ref/jenkins_at_gitlab.key
COPY plugins.txt /usr/share/jenkins/ref/
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/ref/plugins.txt
ENV JAVA_OPTS="-Dhudson.Main.development=true -Djenkins.install.runSetupWizard=false"
ADD init_groovy_scripts /usr/share/jenkins/ref/init.groovy.d/
