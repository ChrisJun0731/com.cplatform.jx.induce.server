package com.cplatform.jx.induce.server.protocol.dirty_words_filter;


import java.util.Arrays;
import org.apache.log4j.Logger;
import com.cplatform.jx.induce.server.service.ActInduceDaoImpl;

/**
 * 敏感字过滤类. <br>
 * <p>
 * Copyright: Copyright (c) 2009-7-31 下午04:35:52
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class DirtyWordsFilter {

	/**
	 * 敏感字缓存定制加载线程. <br>
	 */
	static class DirtyWordsBufferLoader extends Thread {

		/**
		 * 构造函数
		 */
		public DirtyWordsBufferLoader() {
			setName("DirtyWordsBufferLoader");
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000 * 60 * 60);
					loadDirtyWordsBuffer();
				}
				catch (Exception ex) {
				}
			}
		}
	}

	private static ActInduceDaoImpl dao =new ActInduceDaoImpl();
	/** 关键字缓存 */
	private static DirtyWordsBuffer dirtyWordsBuffer;

	/** 日志记录器 */
	private static Logger logger = Logger.getLogger("DirtyWordsBufferLoader");

	/**
	 * 删除指定内容中的关键字。循环删除，直至没有关键字为止。
	 * 
	 * @param text
	 *            原始文本内容
	 * @return 返回删除了关键字的字符串
	 */
	private static String deleteAllWords(char[] text) {
		while (true) {
			char[] tempChars = deleteAllWordsOnce(text, dirtyWordsBuffer);
			if (!Arrays.equals(tempChars, text)) {
				text = tempChars;
				continue;
			} else {
				break;
			}
		}
		return new String(text);
	}

	/**
	 * 删除指定内容中的关键字。循环删除，直至没有关键字为止。
	 * 
	 * @param text
	 *            原始文本内容
	 * @return 返回删除了关键字的字符串
	 */
	public static String deleteAllWords(String text) {
		try {
			return deleteAllWords(text.toCharArray());
		}
		catch (Exception ex) {
			logger.error("过滤关键字出错", ex);
			return text;
		}
	}

	/**
	 * 删除指定内容中的关键字。只删除一次，不循环删除。
	 * 
	 * @param text
	 *            原始文本内容
	 * @param wordsBuffer
	 *            关键字缓冲
	 * @return 返回删除了关键字的字符串
	 */
	private static char[] deleteAllWordsOnce(char[] text, DirtyWordsBuffer wordsBuffer) {
		//
		char[] tempChars = new char[text.length];
		if (tempChars.length < text.length) {
			tempChars = new char[text.length];
		}
		//
		char c;
		int textIndex = 0;
		int tempCharsSize = 0;
		while (true) {
			if (textIndex >= text.length) {
				break;
			}
			c = text[textIndex];
			boolean found = false;
			char[][] words = wordsBuffer.getWordList(c);
			if (words != null) {
				for (char[] word : words) {
					int endIndex = isStartWith(text, word, textIndex);
					if (endIndex >= 0) {
						textIndex = endIndex;
						found = true;
						if (logger.isDebugEnabled()) {
							logger.info("过滤关键字:" + new String(word) + ", text:" + new String(text));
						}
						break;
					}
				}
			}
			if (!found) {
				tempChars[tempCharsSize] = c;
				tempCharsSize++;
				textIndex++;
			} else {
			}
		}
		char[] result = Arrays.copyOf(tempChars, tempCharsSize);
		return result;
	}

	/**
	 * 初始化对象实例
	 * 
	 * @throws Exception
	 */
	public synchronized static void init() throws Exception {
		loadDirtyWordsBuffer();
		new DirtyWordsBufferLoader().start();
	}

	/**
	 * 判断第一个字符串的指定位置是否以第二个字符串开头。并且忽略无效字符。
	 * 
	 * @param cs1
	 *            第一个字符串
	 * @param cs2
	 *            第二个字符
	 * @param fromIndex
	 *            第一个字符串的指定位置
	 * @return 如非指定字符大头，则返回-1。否则返回指定字符的末尾位置。
	 */
	private static int isStartWith(char[] cs1, char[] cs2, int fromIndex) {
		if (cs1 == null || cs2 == null) {
			return -1;
		}
		// 剩余长度
		if (cs1.length - fromIndex < cs2.length) {
			return -1;
		}
		//
		int index1 = fromIndex;
		int index2 = 0;
		int space = 0;
		while (true) {
			if (index1 >= cs1.length) {
				return  -1;
			}
			char c = cs1[index1];
			// 跳过无效字符，并转换大小写、半角全角
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
				// 其他字符不处理
				index1++;
				continue;
			}
			// 如果是
			if (cs2[index2] == '*') {
				space = 5;
				index2++;
			} else if (cs2[index2] == '.') {
				space = 1;
				index2++;
			}
			if (c != cs2[index2]) {
				space--;
				if (space < 0) {
					return -1;
				} else {
					index1++;
				}
			} else {
				index1++;
				index2++;
				space = 0;
			}
			if (index2 >= cs2.length) {
				break;
			}
		}
		return index1;
	}

	private static void loadDirtyWordsBuffer() throws Exception {
		logger.info("开始加载敏感字数据");
//		String sql = "select word from t_sys_filter_word";
//		Connection conn = _dba.getConnection();
		try {
//			PreparedStatement st = conn.prepareStatement(sql);
//			try {
//				ResultSet rs = st.executeQuery();
//				dirtyWordsBuffer = new DirtyWordsBuffer(rs);
//			}
//			catch (Exception ex) {
//				throw ex;
//			}
//			finally {
//				st.close();
//			}
			dirtyWordsBuffer = dao.loadDirtyWordsBuffer();
		}
		catch (Exception ex) {
			logger.error("敏感字出现异常", ex);
			throw ex;
		}
//		finally {
//			_dba.freeConnection(conn);
//		}
		logger.info("敏感字加载完毕");
	}

}
