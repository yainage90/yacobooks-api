package com.yaincoding.yacobooksapi.domain.book.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.SearchHitStage;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

	private static final int COUNT_PER_PAGE = 10;

	@Value("${elasticsearch.index.book.name}")
	private String BOOK_INDEX;

	private final RestHighLevelClient esClient;

	@Autowired
	public BookServiceImpl(RestHighLevelClient esClient) {
		this.esClient = esClient;
	}

	@Override
	public Book getById(String id) {
		GetRequest request = new GetRequest(BOOK_INDEX, id);
		GetResponse response;
		try {
			response = esClient.get(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("IOException occured.");
			return null;
		}

		if (!response.isExists()) {
			return null;
		}

		Book book = new Gson().fromJson(response.getSourceAsString(), Book.class);

		return book;
	}

	@Override
	public BookSearchResponseDto search(BookSearchRequestDto bookSearchRequestDto) {

		try {
			SearchRequest searchRequest = createTitleSearchRequest(bookSearchRequestDto);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				return createBookSearchResponseDto(response, SearchHitStage.TITLE_SEARCH.getStage());
			}
		} catch (IOException e) {
			log.error(
					"query=" + bookSearchRequestDto.getQuery() + ", page=" + bookSearchRequestDto.getPage(),
					e);
			BookSearchResponseDto responseDto = new BookSearchResponseDto();
			responseDto.setTotalHits(-1);
			responseDto.setBooks(Collections.emptyList());
			return responseDto;
		}

		try {
			SearchRequest searchRequest = createTitleSpellCorrectSearchRequest(bookSearchRequestDto);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				return createBookSearchResponseDto(response, SearchHitStage.SPELL_CORRECT_SEARCH.getStage());
			}
		} catch (IOException e) {
			log.error(
					"query=" + bookSearchRequestDto.getQuery() + ", page=" + bookSearchRequestDto.getPage(),
					e);
			BookSearchResponseDto responseDto = new BookSearchResponseDto();
			responseDto.setTotalHits(-1);
			responseDto.setBooks(Collections.emptyList());
			return responseDto;
		}

		try {
			SearchRequest searchRequest = createTitleNgramSearchRequest(bookSearchRequestDto);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			return createBookSearchResponseDto(response, SearchHitStage.NGRAM_SEARCH.getStage());
		} catch (IOException e) {
			log.error(
					"query=" + bookSearchRequestDto.getQuery() + ", page=" + bookSearchRequestDto.getPage(),
					e);
			BookSearchResponseDto responseDto = new BookSearchResponseDto();
			responseDto.setTotalHits(-1);
			responseDto.setBooks(Collections.emptyList());
			return responseDto;
		}
	}

	private SearchRequest createTitleSearchRequest(BookSearchRequestDto bookSearchRequestDto) {

		String query = bookSearchRequestDto.getQuery();
		int page = bookSearchRequestDto.getPage();

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.termQuery("title", query).boost(10.0f));
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("title_text", query).boost(5.0f));

		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(COUNT_PER_PAGE * (page - 1));
		searchSourceBuilder.size(COUNT_PER_PAGE);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchRequest createTitleSpellCorrectSearchRequest(
			BookSearchRequestDto bookSearchRequestDto) {
		String query = bookSearchRequestDto.getQuery();
		int page = bookSearchRequestDto.getPage();

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("title_spell", query).boost(2.0f));

		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(COUNT_PER_PAGE * (page - 1));
		searchSourceBuilder.size(COUNT_PER_PAGE);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchRequest createTitleNgramSearchRequest(BookSearchRequestDto bookSearchRequestDto) {
		String query = bookSearchRequestDto.getQuery();
		int page = bookSearchRequestDto.getPage();

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("title_ngram", query).boost(2.0f));
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("author_text", query).boost(1.0f));
		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(COUNT_PER_PAGE * (page - 1));
		searchSourceBuilder.size(COUNT_PER_PAGE);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private BookSearchResponseDto createBookSearchResponseDto(SearchResponse response,
			String stage) {
		BookSearchResponseDto responseDto = new BookSearchResponseDto();
		responseDto.setSearchHitStage(stage);
		responseDto.setTotalHits(response.getHits().getTotalHits().value);
		responseDto.setBooks(Arrays.stream(response.getHits().getHits())
				.map(hit -> new Gson().fromJson(hit.getSourceAsString(), Book.class))
				.collect(Collectors.toList()));

		return responseDto;
	}
}
