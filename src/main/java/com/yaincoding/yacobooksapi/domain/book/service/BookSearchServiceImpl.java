package com.yaincoding.yacobooksapi.domain.book.service;

import java.io.IOException;
import com.google.gson.Gson;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.SearchHitStage;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;
import com.yaincoding.yacobooksapi.slack.SlackLogBot;
import com.yaincoding.yacobooksapi.util.HangulUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public final class BookSearchServiceImpl implements BookSearchService {

	private final RestHighLevelClient esClient;
	private final BookHelper bookHelper;

	@Override
	public Book getById(String id) throws BookSearchException {

		GetResponse response;
		try {
			response = esClient.get(bookHelper.createGetByIdRequest(id), RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("IOException occured.");
			SlackLogBot.sendError(e);
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

		String query = bookSearchRequestDto.getQuery();
		int page = bookSearchRequestDto.getPage();

		BookSearchResponseDto bookSearchResponseDto;

		if (HangulUtil.isCompleteHangulQuery(query)) {
			bookSearchResponseDto = searchTitle(query, page);
			if (bookSearchResponseDto.getTotalHits() > 0) {
				return bookSearchResponseDto;
			}

			bookSearchResponseDto = searchTitleAuthor(query, page);
			if (bookSearchResponseDto.getTotalHits() > 0) {
				return bookSearchResponseDto;
			}
		}

		if (HangulUtil.isChosungQuery(bookSearchRequestDto.getQuery())) {
			bookSearchResponseDto = searchByChosung(query, page);
			if (bookSearchResponseDto.getTotalHits() > 0) {
				return bookSearchResponseDto;
			}
		}

		if (HangulUtil.isEnglishQuery(query)) {
			bookSearchResponseDto = searchEngToHan(query, page);
			if (bookSearchResponseDto.getTotalHits() > 0) {
				return bookSearchResponseDto;
			}
		}

		bookSearchResponseDto = searchHanToEng(query, page);
		if (bookSearchResponseDto.getTotalHits() > 0) {
			return bookSearchResponseDto;
		}

		return BookSearchResponseDto.emptyResponse();
	}

	private BookSearchResponseDto searchTitle(String query, int page) {
		try {
			SearchRequest searchRequest = bookHelper.createTitleSearchRequest(query, page);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = bookHelper.createBookSearchResponseDto(response,
						SearchHitStage.TITLE_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error("query=" + query + ", page=" + page, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		return BookSearchResponseDto.emptyResponse();
	}

	private BookSearchResponseDto searchTitleAuthor(String query, int page) {
		try {
			SearchRequest searchRequest = bookHelper.createTitleAuthorSearchRequest(query, page);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = bookHelper.createBookSearchResponseDto(response,
						SearchHitStage.TITLE_AUTHOR_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error("query=" + query + ", page=" + page, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		return BookSearchResponseDto.emptyResponse();
	}

	private BookSearchResponseDto searchByChosung(String query, int page) {
		try {
			String[] includes =
					{"isbn13", "title", "author", "publisher", "pubDate", "imageUrl", "description"};
			SearchRequest searchRequest = bookHelper.createChosungSearchRequest(query, page, includes);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = bookHelper.createBookSearchResponseDto(response,
						SearchHitStage.CHOSUNG_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error("query=" + query + ", page=" + page, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		return BookSearchResponseDto.emptyResponse();
	}

	private BookSearchResponseDto searchHanToEng(String query, int page) {
		try {
			String[] includes =
					{"isbn13", "title", "author", "publisher", "pubDate", "imageUrl", "description"};
			SearchRequest searchRequest = bookHelper.createHanToEngSearchRequest(query, page, includes);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = bookHelper.createBookSearchResponseDto(response,
						SearchHitStage.ENG_TO_HAN_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error("query=" + query + ", page=" + page, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		return BookSearchResponseDto.emptyResponse();
	}

	private BookSearchResponseDto searchEngToHan(String query, int page) {
		try {
			String[] includes =
					{"isbn13", "title", "author", "publisher", "pubDate", "imageUrl", "description"};
			SearchRequest searchRequest = bookHelper.createEngToHanSearchRequest(query, page, includes);
			SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
			if (response.getHits().getTotalHits().value > 0) {
				BookSearchResponseDto responseDto = bookHelper.createBookSearchResponseDto(response,
						SearchHitStage.ENG_TO_HAN_SEARCH.getStage());

				log.debug(responseDto.toString());

				return responseDto;
			}
		} catch (IOException e) {
			log.error("query=" + query + ", page=" + page, e);
			SlackLogBot.sendError(e);
			throw new BookSearchException("엘라스틱서치 Search API 호출 에러");
		}

		return BookSearchResponseDto.emptyResponse();
	}
}
