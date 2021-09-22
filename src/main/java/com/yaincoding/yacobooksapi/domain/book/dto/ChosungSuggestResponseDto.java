package com.yaincoding.yacobooksapi.domain.book.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChosungSuggestResponseDto {
	
	private String result;
	private List<String> titles;
}
