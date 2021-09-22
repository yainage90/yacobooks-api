package com.yaincoding.yacobooksapi.domain.book.controller;

import com.yaincoding.yacobooksapi.domain.book.dto.AutoCompleteResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;
import com.yaincoding.yacobooksapi.domain.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Book> getBook(@PathVariable String id) {
		return ResponseEntity.ok(bookService.getById(id));
	}

	@GetMapping(value = "/search")
	public ResponseEntity<BookSearchResponseDto> search(BookSearchRequestDto bookSearchRequestDto) {
		return ResponseEntity.ok(bookService.search(bookSearchRequestDto));
	}

	@GetMapping(value = "/ac")
	public ResponseEntity<AutoCompleteResponseDto> autoComplete(String query) {
		return ResponseEntity.ok(bookService.autoComplete(query));
	}

	@ExceptionHandler(BookSearchException.class)
	public ResponseEntity<String> handleSearchException() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
	}
}
