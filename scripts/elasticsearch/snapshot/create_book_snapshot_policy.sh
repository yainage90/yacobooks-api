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
    "indices": ["book"]
  },
  "retention": {
    "expire_after": "10d",
    "min_count": 5,
    "max_count": 10
  }
}'
