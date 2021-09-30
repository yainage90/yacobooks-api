#!/usr/bin/bash

ELASTICSEARCH_HOST="http://localhost:9200/_slm/policy/book_snapshot_policy?pretty"

curl -XPUT  $ELASTICSEARCH_HOST \
-u "${ES_USER}:${ES_PASSWORD}" \
-H "Content-Type: application/json" \
-d \
'{
	"schedule": "0 */20 * * * ?",
  "name": "<book-snapshot-{now/m{yyyyMMddHHmm|+09:00}}>",
  "repository": "book_backup",
  "config": {
    "indices": ["book"],
    "ignore_unavailable": true,
    "include_global_state": false,
    "compress": true
  },
  "retention": {
    "expire_after": "10d",
    "min_count": 2,
    "max_count": 4 
  }
}'
