package com.yaincoding.yacobooksapi.domain.book.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SuggestResponseDto {
	
	private String result;
	private List<String> titles;
}
