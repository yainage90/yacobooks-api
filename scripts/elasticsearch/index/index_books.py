#!/usr/bin/python3

from elasticsearch import Elasticsearch
from elasticsearch.helpers import streaming_bulk
import argparse
import os

import pandas as pd

def index(data_dir):
  fnames = os.listdir(data_dir)
  for fname in fnames:
    print('reading {}'.format(fname))
    docs = list()
    fpath = os.path.join(data_dir, fname)
    count = 0
    for chunk in pd.read_csv(fpath, skip_blank_lines=True, quotechar= '"', error_bad_lines=False, chunksize=100000):
      count += 1
      chunk.fillna('', inplace=True)
      for idx, row in chunk.iterrows():
        source = makeDoc(row)
        if not (source['isbn13'] and source['title'] and source['author'] and source['publisher']):
          continue
        doc = {'_id': source['isbn13'], '_index': 'book', '_source': source}
        docs.append(doc)
      for success, info in streaming_bulk(client=esClient, actions=docs, raise_on_error=False, raise_on_exception=False, request_timeout=60, chunk_size=50, max_retries=3):
        if not success:
            try:
              print(info) 
            except UnicodeEncodeError as e:
              print(e)

      docs.clear()
      print('processed {} lines'.format(count * 100000))


def makeDoc(row):
  isbn13 = row['isbn13']
  title = row['title']
  author = row['author']
  publisher = row['publisher']
  pubDate = row['pub_date']
  imageUrl = row['img_url']
  description = row['description']

  source = {
    'isbn13': isbn13,
    'title': title,
    'author': author,
    'publisher': publisher,
    'pubDate': pubDate,
    'imageUrl': imageUrl,
    'description': description,
  }

  return source


if __name__ == "__main__":

  parser = argparse.ArgumentParser()
  parser.add_argument('--data_dir', help='data files directory path')
  parser.add_argument('--user', help='elasticsearch username is needed')
  parser.add_argument('--password', help='elasticsearch password is need')

  args = parser.parse_args()

  data_dir = args.data_dir
  user = args.user
  password = args.password

  es_host= os.environ['ES_HOST'] + ":" + os.environ['ES_PORT']
  esClient = Elasticsearch(hosts=[es_host], http_auth=(user, password), timeout=60)

  index(data_dir)
