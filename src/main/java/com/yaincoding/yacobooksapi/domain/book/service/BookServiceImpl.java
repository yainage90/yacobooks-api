package com.yaincoding.yacobooksapi.domain.book.service;

import java.io.IOException;
import com.google.gson.Gson;
import com.yaincoding.yacobooksapi.domain.book.dto.AutoCompleteResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.SearchHitStage;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;
import com.yaincoding.yacobooksapi.util.BookSearchUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

	private final RestHighLevelClient esClient;

	@Autowired
	public BookServiceImpl(RestHighLevelClient esClient) {
		this.esClient = esClient;
	}

	@Override
	public Book getById(String id) throws BookSearchException {

		GetResponse response;
		try {
			response = esClient.get(BookSearchUtil.createGetByIdRequest(id), RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("IOException occured.");
			throw new BookSearchException("엘라스틱서치 GET API 호출 에러");
		}

		if (!response.isExists()) {
			return null;
		}

		Book book = new Gson().fromJson(response.getSourceAsString(), Book.class);

		return book;
	}

	@Override
	public BookSearchResponseDto search(BookSearchRequestDto bookSearchRequestDto)
			throws BookSearchException {

		try {
			SearchRequest searchRequest = BookSearchUtil.createTitleSearchRequest(bookSearchRequestDto);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = BookSearchUtil.createBookSearchResponseDto(response,
						SearchHitStage.TITLE_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error(
					"query=" + bookSearchRequestDto.getQuery() + ", page=" + bookSearchRequestDto.getPage(),
					e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		try {
			SearchRequest searchRequest =
					BookSearchUtil.createTitleSpellCorrectSearchRequest(bookSearchRequestDto);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = BookSearchUtil.createBookSearchResponseDto(response,
						SearchHitStage.SPELL_CORRECT_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error(
					"query=" + bookSearchRequestDto.getQuery() + ", page=" + bookSearchRequestDto.getPage(),
					e);

			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		try {
			SearchRequest searchRequest =
					BookSearchUtil.createTitleNgramSearchRequest(bookSearchRequestDto);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			BookSearchResponseDto responseDto = BookSearchUtil.createBookSearchResponseDto(response,
					SearchHitStage.NGRAM_SEARCH.getStage());
			log.debug(responseDto.toString());
			return responseDto;
		} catch (IOException e) {
			log.error(
					"query=" + bookSearchRequestDto.getQuery() + ", page=" + bookSearchRequestDto.getPage(),
					e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}
	}

	@Override
	public AutoCompleteResponseDto autoComplete(String query) throws BookSearchException {
		SearchRequest searchRequest = BookSearchUtil.createAutoCompleteSearchRequest(query);
		try {
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			return BookSearchUtil.createAutoCompleteStrings(response);
		} catch (IOException e) {
			log.error("query=" + query, e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}
	}

}
