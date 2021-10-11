package com.yaincoding.yacobooksapi.domain.book.dto;

public enum SearchHitStage {

	TITLE_SEARCH("title_search"), TITLE_AUTHOR_SEARCH("title_author_search"), CHOSUNG_SEARCH(
			"chosung_search"), ENG_TO_HAN_SEARCH(
					"eng_to_han_search"), HAN_TO_ENG_SEARCH("han_to_eng_search"), NO_RESULT("no_result");

	protected final String STAGE;

	SearchHitStage(String stage) {
		STAGE = stage;
	}

	public String getStage() {
		return STAGE;
	}
}
