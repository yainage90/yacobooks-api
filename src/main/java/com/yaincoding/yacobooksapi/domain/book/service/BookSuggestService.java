package com.yaincoding.yacobooksapi.domain.book.service;

import com.yaincoding.yacobooksapi.domain.book.dto.SuggestResponseDto;
import org.elasticsearch.ElasticsearchException;

public interface BookSuggestService {
	SuggestResponseDto suggest(String query) throws ElasticsearchException;
}
