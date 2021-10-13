package com.yaincoding.yacobooksapi.domain.book.service;

import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import org.elasticsearch.ElasticsearchException;

public interface BookSearchService {

	Book getById(String id) throws ElasticsearchException;

	BookSearchResponseDto search(BookSearchRequestDto bookSearchRequestDto)
			throws ElasticsearchException;
}
