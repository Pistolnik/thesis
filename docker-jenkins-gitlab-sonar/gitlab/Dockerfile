FROM gitlab/gitlab-ce:9.5.1-ce.0

RUN apt-get update -y                               \
    && apt-get install -y --no-install-recommends   \
           jq
ADD generate.sh /opt/gitlab/
ADD entrypoint.sh /opt/gitlab/
CMD ["/opt/gitlab/entrypoint.sh"]
