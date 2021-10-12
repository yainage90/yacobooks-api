package com.yaincoding.yacobooksapi.domain.book.dto;

import java.util.Collections;
import java.util.List;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookSearchResponseDto {

	private String result;
	private long totalHits;
	private List<Book> books;
	private String searchHitStage;

	public static BookSearchResponseDto emptyResponse() {
		BookSearchResponseDto responseDto = new BookSearchResponseDto();
		responseDto.setResult("OK");
		responseDto.setTotalHits(0);
		responseDto.setBooks(Collections.emptyList());
		responseDto.setSearchHitStage(SearchHitStage.NO_RESULT.toString());

		return responseDto;
	}
}
