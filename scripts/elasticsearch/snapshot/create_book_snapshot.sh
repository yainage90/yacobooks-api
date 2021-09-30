#!/usr/bin/bash

CURRENT=$(date +%Y%m%d%H%M%S -d '+9 hour')
ELASTICSEARCH_HOST="http://localhost:9200/_snapshot/book_backup/book_backup_$CURRENT?wait_for_completion=true"

curl -XPUT  $ELASTICSEARCH_HOST \
-u "${ES_USER}:${ES_PASSWORD}" \
-H "Content-Type: application/json" \
-d \
'{
	"indices": "book",
	"ignore_unavailable": true,
	"include_global_state": false,
	"compress": true
}'