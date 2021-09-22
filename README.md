# 1. 소개

책 검색 프로젝트인 yacobooks의 api 프로젝트.

한글 검색을 위하여 엘라스틱 서치 플러그인을 자체적으로 제작하여 적용하였음.

↓ 자체 제작한 엘라스틱서치 토큰 필터 플러그인 깃허브 주소

[hanhinsam](https://github.com/yaincoding/hanhinsam)

<br>

# 2. 기능

## 1) 검색

책 검색 기능. 제목, 저자명 등의 필드를 통한 검색. 한글 자모 분리, ngram 매칭 등의 다양한 기법을 적용하여 부분 매칭, 오타 교정 기능 포함.

## 2) 자동 완성

자모 분리, ngram 분리 색인 등의 기법을 적용하여 자연스러운 자동 완성 기능 제공

## 3) 초성 추천

초성 추출, 전방위 ngram 분리 색인 등의 기법을 적용하여 자연스러운 초성 단어 추천 기능 제공. 자동완성과 다르게 자연스러운 초성 추천을 위하여 전방위 매칭만 적용.

<br>

# 3. API 명세

### 1) ISBN13 으로 책 정보 조회

+ **요청**

``` http
/api/book/9788993383614
```

+ path variable
  + isbn13: 도서 isbn13

<br>

+ **응답**

``` json
{
  "isbn10": null,
  "isbn13": "9788993383614",
  "title": "(Google) 안드로이드 분석과 실습 :안드로이드 OS부터 application까지 ",
  "author": "남상엽,김인기 공저",
  "publisher": "상학당",
  "pubDate": "2009",
  "imageUrl": "https://bookthumb-phinf.pstatic.net/cover/060/740/06074095.jpg?type=m1&udate=20141122",
  "description": "『안드로이드 분석과 실습』(CD1장포함)은 처음 기초 전자, 전기, 통신, 컴퓨터, 제어 및 응용을 접하는 학생에게 기본적이며 쉽고 빠르게 접근할 수 있도록 초점을 맞춘 교재이다.",
  "links": null

```

<br>

### 2) 책 검색

+ **요청**

``` http
/api/book/search?query=자바 안드로이드&page=1
```

+ params
  + query: 검색 쿼리
  + page: 페이지

<br>

+ **응답**

``` json
{
  "result": "OK",
  "totalHits": 35,
  "books": [
    {
      "isbn10": null,
      "isbn13": "9788993383614",
      "title": "(Google) 안드로이드 분석과 실습 :안드로이드 OS부터 application까지 ",
      "author": "남상엽,김인기 공저",
      "publisher": "상학당",
      "pubDate": "2009",
      "imageUrl": "https://bookthumb-phinf.pstatic.net/cover/060/740/06074095.jpg?type=m1&udate=20141122",
      "description": "『안드로이드 분석과 실습』(CD1장포함)은 처음 기초 전자, 전기, 통신, 컴퓨터, 제어 및 응용을 접하는 학생에게 기본적이며 쉽고 빠르게 접근할 수 있도록 초점을 맞춘 교재이다.",
      "links": null
    },
    {
      "isbn10": null,
      "isbn13": "9788955025385",
      "title": "자바가이드 ",
      "author": "지은이: 김현희,구현회",
      "publisher": "글로벌",
      "pubDate": "2010",
      "imageUrl": "",
      "description": "",
      "links": null
    },
    ...
  ]
}
```

<br>

### 3) 자동 완성

+ **요청**

``` http
/api/book/ac?query=자바 프로
```

+ params
  + query: 사용자 입력 쿼리

<br>

+ **응답**

``` json
{
  "result": "OK",
  "titles": [
    "자바 프로그래밍:기초부터 모바일까지",
    "고급 자바 프로그래밍 =Advanced Java programming ",
    "자바 프로그래밍 =Java programming ",
    "자바 프로그래밍 실습:이클립스를 이용한 프로그래밍 해설",
    "Java 프로그래밍 실습 :자바프로그램실습 교재 ",
    "(휴대폰을 위한) 모바일 자바 프로그래밍",
    "자바 프로그래밍 기초:원리부터 응용까지",
    "자바 프로그래밍 =Java programming ",
    "(문제 해결 중심의) 자바 프로그래밍 =Java programming ",
    "자바 프로젝트 설계와 구현=Java project design & implementaion"
  ]
}
```

<br>

### 4) 초성 추천

+ **요청**

``` http
/api/book/chosung?query=ㅇㄷㄹㅇㄷ
```

+ params
  + query: 사용자 입력 쿼리

<br>

+ **응답**

``` json
{
  "result": "OK",
  "titles": [
    "(안드로이드 플랫폼과 응용을 함께 배우는) 임베디드 프로그래밍 ",
    "엔드 리 엔드 =End re end",
    "아들러 아동상담=이론과 실제/Adlerian child counseling",
    "엔드 리 엔드 =End re end",
    "안드리아 대륙기 =임중상 퓨전 판타지 장편소설.The andria story "
  ]
}
```