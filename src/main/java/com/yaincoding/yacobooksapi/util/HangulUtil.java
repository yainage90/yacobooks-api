package com.yaincoding.yacobooksapi.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	public static final Set<Character> CHOSUNG_SET = new HashSet<>() {
		{
			add(' ');
			add('ㄱ');
			add('ㄴ');
			add('ㄷ');
			add('ㄹ');
			add('ㅁ');
			add('ㅂ');
			add('ㅅ');
			add('ㅇ');
			add('ㅈ');
			add('ㅊ');
			add('ㅋ');
			add('ㅌ');
			add('ㅍ');
			add('ㅎ');
			add('ㄲ');
			add('ㄸ');
			add('ㅃ');
			add('ㅆ');
			add('ㅉ');
			add('ㄳ');
			add('ㄵ');
			add('ㄶ');
			add('ㄺ');
			add('ㄻ');
			add('ㄼ');
			add('ㄽ');
			add('ㄾ');
			add('ㄿ');
			add('ㅀ');
			add('ㅄ');
		}
	};

	public static boolean isChosungQuery(String query) {
		for (char ch : query.toCharArray()) {
			if (!CHOSUNG_SET.contains(ch)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEnglishQuery(String query) {
		for (char ch : query.toCharArray()) {
			if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))) {
				return false;
			}
		}
		return true;
	}
}
