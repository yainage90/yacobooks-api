package com.yaincoding.yacobooksapi.domain.book.dto;

public enum SearchHitStage {

	TITLE_SEARCH("title_search"), SPELL_CORRECT_SEARCH("title_spell_correct_search"), NGRAM_SEARCH(
			"title_ngram_search");

	protected final String STAGE;

	SearchHitStage(String stage) {
		STAGE = stage;
	}

	public String getStage() {
		return STAGE;
	}
}
