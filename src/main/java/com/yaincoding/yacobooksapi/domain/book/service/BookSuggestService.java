package com.yaincoding.yacobooksapi.domain.book.service;

import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;

public interface BookSuggestService {
	SuggestResponseDto suggest(String query) throws BookSearchException;
}
