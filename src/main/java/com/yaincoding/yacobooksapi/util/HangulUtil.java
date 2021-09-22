package com.yaincoding.yacobooksapi.util;

import java.util.HashMap;
import java.util.Map;

public class HangulUtil {

	private static final Map<Character, String> layeredJaumMap = new HashMap<>() {
		{
			put('ㄳ', "ㄱㅅ");
			put('ㄵ', "ㄴㅈ");
			put('ㄺ', "ㄹㄱ");
			put('ㄻ', "ㄹㅁ");
			put('ㄼ', "ㄹㅂ");
			put('ㄾ', "ㄹㅌ");
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
