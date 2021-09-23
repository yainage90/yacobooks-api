from elasticsearch import Elasticsearch, helpers
import argparse
import os

import pandas as pd

def index(data_dir):
  fnames = os.listdir(data_dir)
  for fname in fnames:
    print('reading {}'.format(fname))
    docs = list()
    fpath = os.path.join(data_dir, fname)
    df = pd.read_csv(fpath, skip_blank_lines=True, quotechar= '"', error_bad_lines=False)
    df.fillna('', inplace=True)
    count = 0
    for idx, row in df.iterrows():
      count += 1
      if count % 10000 == 0:
        print('readed {} lines'.format(count))
      source = makeDoc(row)
      if not source['isbn13']:
        continue
      doc = {'_id': source['isbn13'], '_index': 'book', '_source': source}
      docs.append(doc)
      if len(docs) >= 100:
        helpers.bulk(esClient, docs)
        docs.clear()
    if len(docs) > 0:
      helpers.bulk(esClient, docs)
      docs.clear()


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

  elasticsearch_host = ':'.join(os.environ['ES_HOST'], os.environ['ES_PORT'])
  esClient = Elasticsearch(hosts=[elasticsearch_host], http_auth=(user, password), timeout=60)

  index(data_dir)
