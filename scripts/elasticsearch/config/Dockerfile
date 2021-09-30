ARG ELK_VERSION

FROM docker.elastic.co/elasticsearch/elasticsearch:${ELK_VERSION}

COPY plugins/hanhinsam-0.1.zip /plugins/hanhinsam.zip

RUN elasticsearch-plugin install analysis-nori
RUN elasticsearch-plugin install file:///plugins/hanhinsam.zip
RUN elasticsearch-plugin install --batch repository-gcs
RUN elasticsearch-keystore create
RUN elasticsearch-keystore add-file gcs.client.default.credentials_file /keystore/gcs_account_file.json