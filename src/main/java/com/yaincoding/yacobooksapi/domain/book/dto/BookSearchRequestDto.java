package com.yaincoding.yacobooksapi.domain.book.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookSearchRequestDto {
	
	private String query;
	private int page;
}
