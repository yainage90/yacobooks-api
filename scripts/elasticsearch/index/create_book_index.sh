#!/usr/bin/bash

ELASTICSEARCH_HOST="$ES_HOST:$ES_PORT/book?pretty"

curl -XPUT  $ELASTICSEARCH_HOST \
-u "${ES_USER}:${ES_PASSWORD}" \
-H "Content-Type: application/json" \
-d \
'{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 0,
    "index.max_ngram_diff": 50,
    "analysis": {
      "char_filter": {
        "white_remove_char_filter": {
          "type": "pattern_replace",
          "pattern": "\\s+",
          "replacement": ""
        },
        "special_character_filter": {
          "pattern": "[^\\p{L}\\p{Nd}\\p{Blank}]",
          "type": "pattern_replace",
          "replacement": ""
        },
        "author_trivial_filter": {
          "type": "mapping",
          "mappings": [
            "edited by => ",
            "지음 => ",
            "옮김 => ",
            "지은이 => ",
            "일러스트 => ",
            "editors => ",
            "이 책을 쓰신 분 => ",
            "글쓴이 => ",
            "만화 => ",
            "번역 => ",
            "글 => ",
            "그림: => ",
            "저자 => ",
            "엮은이 => ",
            "편저자 => ",
            "구성: => ",
            "연구책임자 => ",
            "연구자 => ",
            "디자인 => ",
            "편집 => ",
            "편저 => "
          ]
        }
      },
      "tokenizer": {
				"title_nori_tokenizer": {
					"type": "nori_tokenizer",
					"decompound_mode": "mixed",
					"discard_punctuation": "true"
				}
			},
      "filter": {
        "ngram4_filter": {
          "type": "ngram",
          "min_gram": 4,
          "max_gram": 50
        },
        "ngram2_filter": {
          "type": "ngram",
          "min_gram": 2,
          "max_gram": 10
        },
        "edge_ngram_front_filter": {
          "type": "edge_ngram",
          "min_gram": 2,
          "max_gram": 10
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
        "title_nori_analyzer": {
          "type": "custom",
          "tokenizer": "title_nori_tokenizer",
          "filter": [
            "lowercase",
						"nori_readingform"
          ]
        },
        "title_ngram_analyzer": {
          "type": "custom",
          "char_filter": [
            "white_remove_char_filter",
            "special_character_filter"
          ],
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "ngram2_filter"
          ]
        },
        "title_spell_analyzer": {
          "type": "custom",
          "char_filter": [
            "white_remove_char_filter",
            "special_character_filter"
          ],
          "tokenizer": "keyword",
          "filter": [
            "lowercase",
            "hanhinsam_jamo"
          ]
        },
        "ac_analyzer": {
          "type": "custom",
          "char_filter": [
            "white_remove_char_filter",
            "special_character_filter"
          ],
          "tokenizer": "keyword",
          "filter": [
            "lowercase",
            "hanhinsam_jamo",
            "ngram4_filter"
          ]
        },
        "chosung_index_analyzer": {
          "type": "custom",
          "char_filter": [
            "white_remove_char_filter",
            "special_character_filter"
          ],
          "tokenizer": "keyword",
          "filter": [
            "lowercase",
            "hanhinsam_chosung",
            "edge_ngram_front_filter"
          ]
        },
        "chosung_search_analyzer": {
          "type": "custom",
          "char_filter": [
            "white_remove_char_filter",
            "special_character_filter"
          ],
          "tokenizer": "keyword",
          "filter": [
            "lowercase"
          ]
        },
        "hantoeng_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "char_filter": [
            "special_character_filter"
          ],
          "filter": [
            "lowercase",
            "hanhinsam_hantoeng"
          ]
        },
        "engtohan_analyzer": {
          "type": "custom",
          "char_filter": [
            "special_character_filter"
          ],
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "hanhinsam_engtohan"
          ]
        },
        "author_index_analyzer": {
          "type": "custom",
          "char_filter": [
            "author_trivial_filter",
            "special_character_filter",
            "white_remove_char_filter"
          ],
          "tokenizer": "keyword",
          "filter": [
            "ngram2_filter"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "isbn10": {
        "type": "keyword"
      },
      "isbn13": {
        "type": "keyword"
      },
      "title": {
        "type": "keyword",
        "copy_to": ["title_text", "title_ngram", "title_spell", "title_ac", "title_chosung", "title_engtohan", "title_hantoeng"]
      },
      "title_text": {
        "type": "text",
        "analyzer": "title_nori_analyzer"
      },
      "title_ngram": {
        "type": "text",
        "analyzer": "title_ngram_analyzer"
      },
      "title_spell": {
        "type": "text",
        "analyzer": "title_spell_analyzer"
      },
      "title_ac": {
        "type": "text",
        "analyzer": "ac_analyzer"
      },
      "title_chosung": {
        "type": "text",
        "analyzer": "chosung_index_analyzer",
        "search_analyzer": "chosung_search_analyzer"
      },
      "title_engtohan": {
        "type": "text",
        "analyzer": "standard", 
        "search_analyzer": "engtohan_analyzer"
      },
      "title_hantoeng": {
        "type": "text",
        "analyzer": "standard", 
        "search_analyzer": "hantoeng_analyzer"
      },
      "author": {
        "type": "keyword",
        "copy_to": ["author_text"]
      },
      "author_text": {
        "type": "text",
        "analyzer": "author_index_analyzer",
        "search_analyzer": "standard"
      },
      "publichser": {
        "type": "keyword"
      },
      "pubDate": {
        "type": "keyword"
      },
      "imageUrl": {
        "type": "keyword"
      },
      "description": {
        "type": "keyword"
      },
      "link": {
        "type": "keyword"
      }
    }
  } 
}'