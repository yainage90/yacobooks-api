package com.yaincoding.yacobooksapi.domain.book.search.dto;

public enum SearchHitStage {

	TITLE_SEARCH("title_search"), TITLE_AUTHOR_SEARCH("title_author_search");

	protected final String STAGE;

	SearchHitStage(String stage) {
		STAGE = stage;
	}

	public String getStage() {
		return STAGE;
	}
}
