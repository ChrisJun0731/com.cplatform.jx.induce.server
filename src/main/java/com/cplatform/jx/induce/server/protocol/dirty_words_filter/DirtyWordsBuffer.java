package com.cplatform.jx.induce.server.protocol.dirty_words_filter;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * �ؼ��ֻ���.
 * <p>
 * Copyright: Copyright (c) 2009-7-31 ����11:56:10
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class DirtyWordsBuffer {

	/**
	 * ������Ч�ַ�
	 * 
	 * @param text
	 *            ��Ҫ���˵�ԭʼ����
	 * @return ���˵���Ч�ַ�������
	 */
	public static char[] filtrateString(char[] text) {
		// �Ӷ�����л�ȡChar[]
		char[] bufferChar = new char[text.length];
		// �жϳ����Ƿ����
		if (bufferChar.length < text.length) {
			bufferChar = new char[text.length];
		}
		int flag = 0;
		// ���������������ַ�
		for (char element : text) {
			if (element >= 65296 && element <= 65305) {
				// ȫ������ת�ɰ��
				element = (char) (element - 65248);
			} else if (element >= 48 && element <= 57) {
				// �������
			} else if (element >= 65313 && element <= 65338) {
				// ȫ�Ǵ�дת���ɰ��Сд
				element = (char) (element - 65216);
			} else if (element >= 65345 && element <= 65370) {
				// ȫ��Сдת���ɰ��Сд
				element = (char) (element - 65248);
			} else if (element >= 12353 && element <= 12436) {
				// ����ƽ����
			} else if (element >= 12449 && element <= 12538) {
				// ����Ƭ����
			} else if (element >= 19968 && element <= 40869) {
				// ����
			} else if (element >= 65 && element <= 90) {
				// ��Ǵ�д��ĸתСд
				element = (char) (element + 32);
			} else if (element >= 97 && element <= 122) {
				// ���Сд��ĸ
			} else if (element == '*' || element == '.') {
				// ���������ַ�
			} else {
				// �����ַ�������
				continue;
			}
			bufferChar[flag] = element;
			flag++;
		}
		// ɾ�������������ַ�
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
		// ɾ�����Ҳ�������ַ�
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
		// �����µ��ַ���
		text = Arrays.copyOfRange(bufferChar, start, end);
		// ����
		return text;
	}

	/** �ؼ������� <���ַ�, ��������嵥> */
	private Map<Character, char[][]> wordMap = new HashMap<Character, char[][]>();

	/** ��ʱ�ؼ������� <���ַ�, ��������嵥> */
	private Map<Character, List<char[]>> wordMapTmp = new HashMap<Character, List<char[]>>();

	/**
	 * ���캯������ָ����BufferedReader�л�ȡ�ؼ��֡�
	 * 
	 * @param reader
	 *            ����Reader
	 * @throws Exception
	 *             �����쳣
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
	 * ���캯������ָ����DataReader�л�ȡ�ؼ��֡�
	 * 
	 * @param reader
	 *            ����Reader
	 * @throws Exception
	 *             �����쳣
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
	 * ���캯������ָ����ResultSet�л�ȡ�ؼ��֡�
	 * 
	 * @param reader
	 *            ���ݵ�ResultSet�����������ڵ�һ��
	 * @throws Exception
	 *             �����쳣
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
	 * ��ӹؼ���
	 * 
	 * @param word
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
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
	 * ��ȡ��ָ�����ַ���ͷ�Ĺؼ����嵥
	 * 
	 * @param c
	 *            ���ַ�
	 * @return �����Ը����ַ���ͷ�Ĺؼ����嵥�����û�У��򷵻�null
	 */
	char[][] getWordList(char c) {
		if (c >= 65296 && c <= 65305) {
			// ȫ������ת�ɰ��
			c = (char) (c - 65248);
		} else if (c >= 48 && c <= 57) {
			// �������
		} else if (c >= 65313 && c <= 65338) {
			// ȫ�Ǵ�дת���ɰ��Сд
			c = (char) (c - 65216);
		} else if (c >= 65345 && c <= 65370) {
			// ȫ��Сдת���ɰ��Сд
			c = (char) (c - 65248);
		} else if (c >= 12353 && c <= 12436) {
			// ����ƽ����
		} else if (c >= 12449 && c <= 12538) {
			// ����Ƭ����
		} else if (c >= 19968 && c <= 40869) {
			// ����
		} else if (c >= 65 && c <= 90) {
			// ��Ǵ�д��ĸתСд
			c = (char) (c + 32);
		} else if (c >= 97 && c <= 122) {
			// ���Сд��ĸ
		} else {
			return null;
		}
		return wordMap.get(c);
	}

	/**
	 * ����ʱ�ؼ�������תΪ��ʽ�ؼ�������
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
