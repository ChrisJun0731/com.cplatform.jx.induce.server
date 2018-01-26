package com.cplatform.jx.induce.server.protocol.dirty_words_filter;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 关键字缓存.
 * <p>
 * Copyright: Copyright (c) 2009-7-31 上午11:56:10
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class DirtyWordsBuffer {

	/**
	 * 过滤无效字符
	 * 
	 * @param text
	 *            需要过滤的原始内容
	 * @return 过滤掉无效字符的内容
	 */
	public static char[] filtrateString(char[] text) {
		// 从对象池中获取Char[]
		char[] bufferChar = new char[text.length];
		// 判断长度是否合适
		if (bufferChar.length < text.length) {
			bufferChar = new char[text.length];
		}
		int flag = 0;
		// 遍历内容中所有字符
		for (char element : text) {
			if (element >= 65296 && element <= 65305) {
				// 全角数字转成半角
				element = (char) (element - 65248);
			} else if (element >= 48 && element <= 57) {
				// 半角数字
			} else if (element >= 65313 && element <= 65338) {
				// 全角大写转换成半角小写
				element = (char) (element - 65216);
			} else if (element >= 65345 && element <= 65370) {
				// 全角小写转换成半角小写
				element = (char) (element - 65248);
			} else if (element >= 12353 && element <= 12436) {
				// 日文平假名
			} else if (element >= 12449 && element <= 12538) {
				// 日文片假名
			} else if (element >= 19968 && element <= 40869) {
				// 汉字
			} else if (element >= 65 && element <= 90) {
				// 半角大写字母转小写
				element = (char) (element + 32);
			} else if (element >= 97 && element <= 122) {
				// 半角小写字母
			} else if (element == '*' || element == '.') {
				// 保留特殊字符
			} else {
				// 其他字符不处理
				continue;
			}
			bufferChar[flag] = element;
			flag++;
		}
		// 删除最左侧的特殊字符
		int start = 0;
		while (true) {
			if (bufferChar[start] == '*' || bufferChar[start] == '.') {
				start++;
			} else {
				break;
			}
			if (start >= flag) {
				break;
			}
		}
		// 删除最右侧的特殊字符
		int end = flag;
		while (true) {
			if (bufferChar[end - 1] == '*' || bufferChar[end - 1] == '.') {
				end--;
			} else {
				break;
			}
			if (end <= start) {
				break;
			}
		}
		// 产生新的字符串
		text = Arrays.copyOfRange(bufferChar, start, end);
		// 返回
		return text;
	}

	/** 关键字索引 <首字符, 相关内容清单> */
	private Map<Character, char[][]> wordMap = new HashMap<Character, char[][]>();

	/** 临时关键字索引 <首字符, 相关内容清单> */
	private Map<Character, List<char[]>> wordMapTmp = new HashMap<Character, List<char[]>>();

	/**
	 * 构造函数。从指定的BufferedReader中获取关键字。
	 * 
	 * @param reader
	 *            内容Reader
	 * @throws Exception
	 *             处理异常
	 */
	public DirtyWordsBuffer(BufferedReader reader) throws Exception {
		String str;
		while (true) {
			str = reader.readLine();
			if (str == null) {
				break;
			} else {
				addWord(str.toCharArray());
			}
		}
		transformWordMap();
	}

	/**
	 * 构造函数。从指定的DataReader中获取关键字。
	 * 
	 * @param reader
	 *            内容Reader
	 * @throws Exception
	 *             处理异常
	 */
//	public DirtyWordsBuffer(DataReader<char[]> reader) throws Exception {
//		char[] str;
//		while (reader.next()) {
//			str = reader.getData();
//			addWord(str);
//		}
//		transformWordMap();
//	}

	/**
	 * 构造函数。从指定的ResultSet中获取关键字。
	 * 
	 * @param reader
	 *            内容的ResultSet，其中内容在第一列
	 * @throws Exception
	 *             处理异常
	 */
	public DirtyWordsBuffer(ResultSet reader) throws Exception {
		String str;
		while (reader.next()) {
			str = reader.getString(1);
			addWord(str.toCharArray());
		}
		transformWordMap();
	}

	/**
	 * 添加关键字
	 * 
	 * @param word
	 *            关键字
	 * @throws Exception
	 *             处理异常
	 */
	private void addWord(char[] word) throws Exception {
		if (word == null) {
			return;
		}
		word = filtrateString(word);
		if (word.length == 0) {
			return;
		}
		List<char[]> wordList = wordMapTmp.get(word[0]);
		if (wordList == null) {
			wordList = new ArrayList<char[]>();
			if (word[0] == '.' || word[0] == '*') {
				wordMapTmp.put(null, wordList);
			} else {
				wordMapTmp.put(word[0], wordList);
			}
			wordList.add(word);
		} else {
			for (char[] w : wordList) {
				if (Arrays.equals(w, word)) {
					return;
				}
			}
			wordList.add(word);
		}
	}

	/**
	 * 获取以指定定字符打头的关键词清单
	 * 
	 * @param c
	 *            首字符
	 * @return 返回以给定字符打头的关键词清单。如果没有，则返回null
	 */
	char[][] getWordList(char c) {
		if (c >= 65296 && c <= 65305) {
			// 全角数字转成半角
			c = (char) (c - 65248);
		} else if (c >= 48 && c <= 57) {
			// 半角数字
		} else if (c >= 65313 && c <= 65338) {
			// 全角大写转换成半角小写
			c = (char) (c - 65216);
		} else if (c >= 65345 && c <= 65370) {
			// 全角小写转换成半角小写
			c = (char) (c - 65248);
		} else if (c >= 12353 && c <= 12436) {
			// 日文平假名
		} else if (c >= 12449 && c <= 12538) {
			// 日文片假名
		} else if (c >= 19968 && c <= 40869) {
			// 汉字
		} else if (c >= 65 && c <= 90) {
			// 半角大写字母转小写
			c = (char) (c + 32);
		} else if (c >= 97 && c <= 122) {
			// 半角小写字母
		} else {
			return null;
		}
		return wordMap.get(c);
	}

	/**
	 * 将临时关键字索引转为正式关键字索引
	 */
	private void transformWordMap() {
		for (Character key : wordMapTmp.keySet()) {
			List<char[]> list = wordMapTmp.get(key);
			char[][] css = new char[list.size()][];
			for (int i = 0; i < css.length; i++) {
				css[i] = list.get(i);
			}
			wordMap.put(key, css);
		}
	}
}
