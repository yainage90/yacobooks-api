package com.yaincoding.yacobooksapi.domain.book.dto;

import java.util.List;
import com.yaincoding.yacobooksapi.domain.book.entity.Book;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookSearchResponseDto {
	
	private long totalHits;
	private List<Book> books;
	private SearchHitStage searchHitStage;
}
