package com.yaincoding.yacobooksapi.domain.book.controller;

import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.exception.BookSearchException;
import com.yaincoding.yacobooksapi.domain.book.service.BookService;
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

	@GetMapping(value = "/suggest")
	public ResponseEntity<SuggestResponseDto> suggest(String query) {
		log.info("/api/book/suggest. query=" + query);
		return ResponseEntity.ok(bookService.suggest(query));
	}

	@ExceptionHandler(BookSearchException.class)
	public ResponseEntity<String> handleSearchException(BookSearchException e) {
		log.error(e.getMessage(), e);
		SlackLogBot.sendError(e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
	}


}
