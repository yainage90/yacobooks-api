package com.yaincoding.yacobooksapi.domain.book.service;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.yaincoding.yacobooksapi.domain.book.dto.AutoCompleteSuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.ChosungSuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class BookSearchHelper {

	private static final String BOOK_INDEX = "book";
	private static final int COUNT_PER_PAGE = 10;
	private static final int AUTO_COMPLETE_LIMIT = 10;

	public GetRequest createGetByIdRequest(String id) {
		return new GetRequest(BOOK_INDEX, id);
	}

	public SearchRequest createTitleSearchRequest(BookSearchRequestDto bookSearchRequestDto) {

		String query = bookSearchRequestDto.getQuery();
		int page = Math.max(1, bookSearchRequestDto.getPage());

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.termQuery("title", query).boost(100.0f));
		boolQueryBuilder.should()
				.add(QueryBuilders.matchQuery("title_text", query).operator(Operator.AND).boost(20.0f));

		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(COUNT_PER_PAGE * (page - 1));
		searchSourceBuilder.size(COUNT_PER_PAGE);
		String[] includes =
				{"isbn13", "title", "author", "publisher", "pubDate", "imageUrl", "description"};
		searchSourceBuilder.fetchSource(includes, null);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	public SearchRequest createTitleAuthorSearchRequest(BookSearchRequestDto bookSearchRequestDto) {
		String query = bookSearchRequestDto.getQuery();
		int page = bookSearchRequestDto.getPage();

		// MultiMatchQueryBuilder multiMatchQueryBuilder =
		// QueryBuilders.multiMatchQuery(query, "title_text", "author_text").operator(Operator.AND);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("title_text", query));
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("author_text", query).boost(10.0f));

		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(COUNT_PER_PAGE * (page - 1));
		searchSourceBuilder.size(COUNT_PER_PAGE);
		String[] includes =
				{"isbn13", "title", "author", "publisher", "pubDate", "imageUrl", "description"};
		searchSourceBuilder.fetchSource(includes, null);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	public SearchRequest createAutoCompleteSearchRequest(String query) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("title_ac", query));
		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(AUTO_COMPLETE_LIMIT);
		String[] includes = {"title"};
		searchSourceBuilder.fetchSource(includes, null);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	public SearchRequest createChosungSearchRequest(String query) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should().add(QueryBuilders.matchQuery("title_chosung", query));
		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(AUTO_COMPLETE_LIMIT);
		String[] includes = {"title"};
		searchSourceBuilder.fetchSource(includes, null);

		SearchRequest searchRequest = new SearchRequest(BOOK_INDEX);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	public BookSearchResponseDto createBookSearchResponseDto(SearchResponse response, String stage) {
		BookSearchResponseDto responseDto = new BookSearchResponseDto();
		responseDto.setResult("OK");
		responseDto.setSearchHitStage(stage);
		responseDto.setTotalHits(response.getHits().getTotalHits().value);
		responseDto.setBooks(Arrays.stream(response.getHits().getHits())
				.map(hit -> new Gson().fromJson(hit.getSourceAsString(), Book.class))
				.collect(Collectors.toList()));

		return responseDto;
	}

	public AutoCompleteSuggestResponseDto createAutoCompleteSuggestResponseDto(
			SearchResponse response) {

		AutoCompleteSuggestResponseDto responseDto = new AutoCompleteSuggestResponseDto();
		responseDto.setResult("OK");
		responseDto.setTitles(Arrays.stream(response.getHits().getHits())
				.map(hit -> hit.getSourceAsMap().get((String) "title").toString())
				.collect(Collectors.toList()));

		return responseDto;
	}

	public ChosungSuggestResponseDto createChosungSuggestResponseDto(SearchResponse response) {

		ChosungSuggestResponseDto responseDto = new ChosungSuggestResponseDto();
		responseDto.setResult("OK");
		responseDto.setTitles(Arrays.stream(response.getHits().getHits())
				.map(hit -> hit.getSourceAsMap().get((String) "title").toString())
				.collect(Collectors.toList()));

		return responseDto;
	}
}
