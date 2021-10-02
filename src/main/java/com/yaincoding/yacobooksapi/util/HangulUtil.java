package com.yaincoding.yacobooksapi.util;

import java.util.HashMap;
import java.util.Map;

public class HangulUtil {

	// prevent instantiate
	private HangulUtil() {}

	private static final Map<Character, String> layeredJaumMap = new HashMap<>() {
		{
			put('ㄳ', "ㄱㅅ");
			put('ㄵ', "ㄴㅈ");
			put('ㄶ', "ㄴㅎ");
			put('ㄺ', "ㄹㄱ");
			put('ㄻ', "ㄹㅁ");
			put('ㄼ', "ㄹㅂ");
			put('ㄽ', "ㄹㅅ");
			put('ㄾ', "ㄹㅌ");
			put('ㄿ', "ㄹㅍ");
			put('ㅀ', "ㄹㅎ");
			put('ㅄ', "ㅂㅅ");
		}
	};

	public static String decomposeLayeredJaum(String query) {
		StringBuilder queryBuilder = new StringBuilder();
		for (char ch : query.toCharArray()) {
			if (layeredJaumMap.containsKey(ch)) {
				queryBuilder.append(layeredJaumMap.get(ch));
			} else {
				queryBuilder.append(ch);
			}
		}

		return queryBuilder.toString();
	}
}
