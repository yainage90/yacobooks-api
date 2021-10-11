package com.yaincoding.yacobooksapi.domain.book.service;

import java.io.IOException;
import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;
import com.yaincoding.yacobooksapi.slack.SlackLogBot;
import com.yaincoding.yacobooksapi.util.HangulUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookSuggestServiceImpl implements BookSuggestService {

	private final RestHighLevelClient esClient;
	private final BookHelper bookHelper;

	@Override
	public SuggestResponseDto suggest(String query) throws BookSearchException {

		if (HangulUtil.isChosungQuery(query)) {
			SuggestResponseDto responseDto = chosungSuggest(query);
			if (hasSuggests(responseDto)) {
				return responseDto;
			}
		} else {
			SuggestResponseDto responseDto = autoComplete(query);
			if (hasSuggests(responseDto)) {
				return responseDto;
			}
		}

		SuggestResponseDto responseDto = hanToEngSuggest(query);
		if (hasSuggests(responseDto)) {
			return responseDto;
		}

		responseDto = engToHanSuggest(query);
		if (hasSuggests(responseDto)) {
			return responseDto;
		}

		return SuggestResponseDto.emptyResponse();
	}

	private SuggestResponseDto chosungSuggest(String query) throws BookSearchException {
		query = HangulUtil.decomposeLayeredJaum(query);
		String[] includes = {"title"};
		SearchRequest searchRequest = bookHelper.createChosungSearchRequest(query, 1, includes);
		try {
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			return bookHelper.createSuggestResponseDto(response);
		} catch (IOException e) {
			log.error("query=" + query, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}
	}

	private SuggestResponseDto autoComplete(String query) throws BookSearchException {
		SearchRequest searchRequest = bookHelper.createAutoCompleteSearchRequest(query);
		try {
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			return bookHelper.createSuggestResponseDto(response);
		} catch (IOException e) {
			log.error("query=" + query, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}
	}

	private SuggestResponseDto hanToEngSuggest(String query) throws BookSearchException {
		String[] includes = {"title"};
		SearchRequest searchRequest = bookHelper.createHanToEngSearchRequest(query, 1, includes);
		try {
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			return bookHelper.createSuggestResponseDto(response);
		} catch (IOException e) {
			log.error("query=" + query, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}
	}

	private SuggestResponseDto engToHanSuggest(String query) throws BookSearchException {
		String[] includes = {"title"};
		SearchRequest searchRequest = bookHelper.createEngToHanSearchRequest(query, 1, includes);
		try {
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			return bookHelper.createSuggestResponseDto(response);
		} catch (IOException e) {
			log.error("query=" + query, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}
	}



	private boolean hasSuggests(SuggestResponseDto suggestResponseDto) {
		return suggestResponseDto.getTitles().size() > 0;
	}
}
