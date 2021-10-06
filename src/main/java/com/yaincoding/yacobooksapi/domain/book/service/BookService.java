package com.yaincoding.yacobooksapi.domain.book.service;

import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;

public interface BookService {

	Book getById(String id) throws BookSearchException;

	BookSearchResponseDto search(BookSearchRequestDto bookSearchRequestDto)
			throws BookSearchException;

	SuggestResponseDto autoComplete(String query) throws BookSearchException;

	SuggestResponseDto chosungSuggest(String query) throws BookSearchException;

}
