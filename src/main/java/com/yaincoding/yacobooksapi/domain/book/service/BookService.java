package com.yaincoding.yacobooksapi.domain.book.service;

import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;

public interface BookService {

	BookSearchResponseDto search(BookSearchRequestDto bookSearchRequestDto);

	Book getById(String id);

}
