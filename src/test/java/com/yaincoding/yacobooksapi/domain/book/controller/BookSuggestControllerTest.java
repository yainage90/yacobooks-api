package com.yaincoding.yacobooksapi.domain.book.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import com.yaincoding.yacobooksapi.domain.book.service.BookSuggestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BookSuggestController.class)
public class BookSuggestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookSuggestService bookSuggestService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		objectMapper = Jackson2ObjectMapperBuilder.json().build();
	}

	@Test
	void testSuggest() throws Exception {
		SuggestResponseDto responseDto = new SuggestResponseDto();
		responseDto.setResult("OK");
		responseDto.setTitles(Collections.singletonList(UUID.randomUUID().toString()));

		given(bookSuggestService.suggest(isA(String.class))).willReturn(responseDto);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/api/book/suggest").param("query", "string");
		mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(content().string(objectMapper.writeValueAsString(responseDto)));
	}
}
