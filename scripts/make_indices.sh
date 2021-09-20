#!/usr/bin/bash

#book
curl -XPUT "http://ec2-13-209-181-246.ap-northeast-2.compute.amazonaws.com:9200/book?pretty" \
-u "${ES_USER}:${ES_PASSWORD}" \
-H "Content-Type: application/json" \
-d \
'{
  "settings": {
    "number_of_shards": 5,
    "number_of_replicas": 1,
		"index.max_ngram_diff": 50,
		"analysis": {
      "char_filter": {
			  "white_remove_char_filter": {
				  "type": "pattern_replace",
				  "pattern": "\\s+",
				  "replacement": ""
		    }
		  },
			"filter": {
				"ngram_filter": {
					"type": "ngram",
					"min_gram": 2,
					"max_gram": 50
				}
			},
			"analyzer": {
				"jamo_analyzer": {
					"type": "custom",
					"tokenizer": "keyword",
					"filter": [
						"hanhinsam_jamo"
					]
				},
        "title_full_analyzer": {
          "type": "custom",
					"char_filter": [
						"white_remove_char_filter"
					],
          "tokenizer": "keyword",
          "filter": [
            "lowercase",
            "hanhinsam_jamo"
          ]
        },
				"title_term_analyzer": {
					"type": "custom",
					"tokenizer": "standard",
					"filter": [
						"lowercase",
						"hanhinsam_jamo",
						"ngram_filter"
					]
				},
			  "ac_analyzer": {
				  "type": "custom",
				  "tokenizer": "keyword",
				  "filter": [
					  "lowercase",
					  "hanhinsam_jamo",
						"ngram_filter"
				  ]
			  },
				"chosung_analyzer": {
					"type": "custom",
					"tokenizer": "keyword",
					"filter": [
						"lowercase",
						"hanhinsam_chosung"
					]
				},
				"hantoeng_analyzer": {
					"type": "custom",
					"tokenizer": "",
					"filter": [
						"lowercase",
						"hanhinsam_hantoeng",
						
					]
				},
				"engtohan_analyzer": {
					"type": "custom",
					"tokenizer": "",
					"filter": [
						"lowercase",
						"hanhinsam_engtohan"
					]
				}
			}
		}
  },
  "mappings": {
    "properties": {
			"isbn13": {
				"type": "keyword"
			},
			"title": {
				"type": "keyword",
				"copy_to": ["title_full", "title_term", "title_ac", "title_chosung", "title_engtohan", "title_hantoeng"]
			},
			"title_full": {
				"type": "text",
				"analyzer": "title_full_analyzer"
			},
			"title_term": {
				"type": "text",
				"analyzer": "title_term_analyzer"
			},
			"title_ac": {
				"type": "text",
				"analyzer": "ac_analyzer",
			},
			"title_chosung": {
				"type": "text",
				"analyzer": "chosung_analyzer",
				"search_analyzer": "keyword_analyzer"
			},
			"title_engtohan": {
				"type": "text",
				"search_analyzer": "engtohan_analyzer"
			},
			"title_hantoeng": {
				"type": "text",
				"search_analyzer": "hantoeng_analyzer"
			}
	  }
  } 
}'