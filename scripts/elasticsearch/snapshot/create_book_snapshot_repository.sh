#!/usr/bin/bash

ELASTICSEARCH_HOST="http://localhost:9200/_snapshot/book_backup?pretty"

curl -X PUT  $ELASTICSEARCH_HOST \
-u "${ES_USER}:${ES_PASSWORD}" \
-H "Content-Type: application/json" \
-d \
'{
  "type": "gcs",
  "settings": {
    "bucket": "yacobooks_snapshot",
    "client": "default",
    "base_path": "book"
  }
}'