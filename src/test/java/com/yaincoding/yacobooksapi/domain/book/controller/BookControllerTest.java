package com.yaincoding.yacobooksapi.domain.book.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchRequestDto;
import com.yaincoding.yacobooksapi.domain.book.dto.BookSearchResponseDto;
import com.yaincoding.yacobooksapi.domain.book.dto.SearchHitStage;
import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import com.yaincoding.yacobooksapi.domain.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest(BookController.class)
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		objectMapper = Jackson2ObjectMapperBuilder.json().build();
	}

	private Book createTestBook() {
		Book book = new Book();
		book.setIsbn10(UUID.randomUUID().toString());
		book.setIsbn13(UUID.randomUUID().toString());
		book.setTitle(UUID.randomUUID().toString());
		book.setAuthor(UUID.randomUUID().toString());
		book.setDescription(UUID.randomUUID().toString());
		book.setImageUrl(UUID.randomUUID().toString());
		book.setPublisher(UUID.randomUUID().toString());
		book.setPubDate(UUID.randomUUID().toString());
		book.setLinks(Collections.singletonList(UUID.randomUUID().toString()));
		return book;
	}

	@Test
	void testGetBook() throws Exception {

		Book book = createTestBook();

		given(bookService.getById(book.getIsbn13())).willReturn(book);

		mockMvc.perform(get("/api/book/" + book.getIsbn13())).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(content().string(objectMapper.writeValueAsString(book)));
	}

	@Test
	void testSearch() throws Exception {
		Book book = createTestBook();

		BookSearchResponseDto responseDto = new BookSearchResponseDto();
		responseDto.setResult("OK");
		responseDto.setTotalHits(1L);
		responseDto.setSearchHitStage(SearchHitStage.TITLE_SEARCH.getStage());
		responseDto.setBooks(Collections.singletonList(book));

		given(bookService.search(isA(BookSearchRequestDto.class))).willReturn(responseDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/book/search")
				.param("query", "string").requestAttr("page", 1);
		mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(content().string(objectMapper.writeValueAsString(responseDto)));
	}


}
