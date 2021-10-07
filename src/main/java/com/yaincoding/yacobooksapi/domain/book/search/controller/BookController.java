package com.yaincoding.yacobooksapi.domain.book.search.controller;

import com.yaincoding.yacobooksapi.domain.book.search.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.search.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.search.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.search.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.search.exception.BookSearchException;
import com.yaincoding.yacobooksapi.domain.book.search.service.BookService;
import com.yaincoding.yacobooksapi.slack.SlackLogBot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {

	private final BookService bookService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Book> getBook(@PathVariable String id) {
		log.info("/api/book" + id);
		return ResponseEntity.ok(bookService.getById(id));
	}

	@GetMapping(value = "/search")
	public ResponseEntity<BookSearchResponseDto> search(BookSearchRequestDto bookSearchRequestDto) {
		log.info("/api/book/search. bookSearchRequestDto=" + bookSearchRequestDto.toString());
		return ResponseEntity.ok(bookService.search(bookSearchRequestDto));
	}

	@GetMapping(value = "/ac")
	public ResponseEntity<SuggestResponseDto> autoComplete(String query) {
		log.info("/api/book/ac. query=" + query);
		return ResponseEntity.ok(bookService.autoComplete(query));
	}

	@GetMapping(value = "/chosung")
	public ResponseEntity<SuggestResponseDto> chosungSuggest(String query) {
		log.info("/api/book/chosung. query=" + query);
		return ResponseEntity.ok(bookService.chosungSuggest(query));
	}

	@ExceptionHandler(BookSearchException.class)
	public ResponseEntity<String> handleSearchException(BookSearchException e) {
		log.error(e.getMessage(), e);
		SlackLogBot.sendError(e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
	}


}
