#/bin/bash

until /opt/gitlab/bin/gitlab-healthcheck --fail &>/dev/null
do
  echo sleeping >> /opt/gitlab/log.log
  sleep 5
done

ADMIN_JSON=$(curl -s http://localhost/api/v4/session --data 'login='"$GITLAB_ADMIN_NAME"'&password='"$GITLAB_ADMIN_PASSWORD"'')
ADMIN_ID=$(echo $ADMIN_JSON | jq -r '.id')
ADMIN_TOKEN=$(echo $ADMIN_JSON | jq -r '.private_token')

# create jenkins user
JENKINS_JSON=$(curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"email":"'"$GITLAB_JENKINS_EMAIL"'", "username":"'"$GITLAB_JENKINS_NAME"'", "name":"'"$GITLAB_JENKINS_NAME"'", "password":"'"$GITLAB_JENKINS_PASSWORD"'", "skip_confirmation":"True"}' "http://localhost/api/v4/users")
JENKINS_ID=$(echo $JENKINS_JSON | jq -r '.id')

# create developer user
DEVELOPER_JSON=$(curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"email":"'"$GITLAB_DEVELOPER_EMAIL"'", "username":"'"$GITLAB_DEVELOPER_NAME"'", "name":"'"$GITLAB_DEVELOPER_NAME"'", "password":"'"$GITLAB_DEVELOPER_PASSWORD"'", "skip_confirmation":"True"}' "http://localhost/api/v4/users")
DEVELOPER_ID=$(echo $DEVELOPER_JSON | jq -r '.id')

# uppload jenkins public key
curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"title":"jenkins_key", "key": "'"$GITLAB_JENKINS_PUBLIC_KEY"'"}' "http://localhost/api/v4/users/$JENKINS_ID/keys"

# get jenkins private token
JENKINS_PRIVATE_TOKEN=$(curl --request POST "http://localhost/api/v4/session?login=$GITLAB_JENKINS_NAME&password=$GITLAB_JENKINS_PASSWORD" | jq -r '.private_token')

# upload the token to jenkins.dev
curl -X POST 'http://'"$JENKINS_ADMIN_NAME"':'"$JENKINS_ADMIN_PASSWORD"'@jenkins.dev:8080/credentials/store/system/domain/_/createCredentials' --data-urlencode 'json={
  "": "0",
  "credentials": {
    "scope": "GLOBAL",
    "id": "jenkins_token",
    "username": "jenkins",
    "password": "'"$JENKINS_PRIVATE_TOKEN"'",
    "description": "credentials for user jenkins at gitlab with token as password",
    "stapler-class": "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl"
  }
}'

# create project
PROJECT=$(curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"name": "test", "visibility":"internal"}' "http://localhost/api/v4/projects/user/$JENKINS_ID")
PROJECT_ID=$(echo $PROJECT | jq -r '.id')

# add developer to have access to the project
$(curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"user_id": "'"$DEVELOPER_ID"'", "access_level":"40"}' "http://localhost/api/v4/projects/$PROJECT_ID/members")

# init repo with readme
git clone http://$GITLAB_JENKINS_NAME:$GITLAB_JENKINS_PASSWORD@localhost/jenkins/test.git
cd test
echo "Git repository for testing purposes" > README.md
git config user.name $GITLAB_JENKINS_NAME
git config user.email $GITLAB_JENKINS_EMAIL
git add .
git commit -m "init commit"
git push origin master

sleep 5

# create hooks
JENKINS_MERGE_REQUEST_JOB_URL="http://"$JENKINS_ADMIN_NAME":"$JENKINS_ADMIN_PASSWORD"@jenkins.dev:8080/project/Test%20Merge%20Request"
MERGE_REQUEST=$(curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"id": "'"$PROJECT_ID"'", "url":"'"$JENKINS_MERGE_REQUEST_JOB_URL"'","merge_requests_events":true, "push_events":false}' "http://localhost/api/v4/projects/$PROJECT_ID/hooks")
MERGE_URL="http://"$JENKINS_ADMIN_NAME":"$JENKINS_ADMIN_PASSWORD"@jenkins.dev:8080/project/Merge%20to%20Master"
MERGE_REQUEST=$(curl -s --request POST -H "Content-Type: application/json" -H "PRIVATE-TOKEN: $ADMIN_TOKEN" --data '{"id": "'"$PROJECT_ID"'", "url":"'"$MERGE_URL"'","merge_requests_events":false, "push_events":true}' "http://localhost/api/v4/projects/$PROJECT_ID/hooks")
