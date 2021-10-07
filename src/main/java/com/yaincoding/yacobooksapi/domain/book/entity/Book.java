package com.yaincoding.yacobooksapi.domain.book.entity;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Book {

	private String isbn10;
	private String isbn13;
	private String title;
	private String author;
	private String publisher;
	private String pubDate;
	private String imageUrl;
	private String description;
	private List<String> links;

	@Override
	public boolean equals(Object obj) {
		return ((Book) obj).getIsbn13() != null && ((Book) obj).getIsbn13() == this.isbn13;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(isbn13);
	}
}
