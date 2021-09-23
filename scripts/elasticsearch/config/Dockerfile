ARG ELK_VERSION

FROM docker.elastic.co/elasticsearch/elasticsearch:${ELK_VERSION}

COPY plugins/hanhinsam-0.1.zip /plugins/hanhinsam.zip

RUN elasticsearch-plugin install analysis-nori
RUN elasticsearch-plugin install file:///plugins/hanhinsam.zip
