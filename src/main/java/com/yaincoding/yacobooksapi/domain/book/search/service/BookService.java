package com.yaincoding.yacobooksapi.domain.book.search.service;

import com.yaincoding.yacobooksapi.domain.book.search.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.search.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.search.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.search.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.search.exception.BookSearchException;

public interface BookService {

	Book getById(String id) throws BookSearchException;

	BookSearchResponseDto search(BookSearchRequestDto bookSearchRequestDto)
			throws BookSearchException;

	SuggestResponseDto autoComplete(String query) throws BookSearchException;

	SuggestResponseDto chosungSuggest(String query) throws BookSearchException;

}
