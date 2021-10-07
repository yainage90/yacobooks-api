package com.yaincoding.yacobooksapi.domain.book.search.dto;

import java.util.List;
import com.yaincoding.yacobooksapi.domain.book.search.entity.Book;
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
}
