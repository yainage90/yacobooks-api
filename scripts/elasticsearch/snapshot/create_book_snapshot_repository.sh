#!/usr/bin/bash

#ELASTICSEARCH_HOST="http://ec2-13-209-181-246.ap-northeast-2.compute.amazonaws.com:9200/book?pretty"
ELASTICSEARCH_HOST="http://localhost:9200/_snapshot/book_backup?pretty"

curl -XPUT  $ELASTICSEARCH_HOST \
-u "${ES_USER}:${ES_PASSWORD}" \
-H "Content-Type: application/json" \
-d \
'{
	"type": "fs",
  "settings": {
    "compress": true,
    "location": "/repo/book"
  }
}'
