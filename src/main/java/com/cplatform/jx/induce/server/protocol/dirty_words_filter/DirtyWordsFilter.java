package com.cplatform.jx.induce.server.protocol.dirty_words_filter;


import java.util.Arrays;
import org.apache.log4j.Logger;
import com.cplatform.jx.induce.server.service.ActInduceDaoImpl;

/**
 * �����ֹ�����. <br>
 * <p>
 * Copyright: Copyright (c) 2009-7-31 ����04:35:52
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class DirtyWordsFilter {

	/**
	 * �����ֻ��涨�Ƽ����߳�. <br>
	 */
	static class DirtyWordsBufferLoader extends Thread {

		/**
		 * ���캯��
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
	/** �ؼ��ֻ��� */
	private static DirtyWordsBuffer dirtyWordsBuffer;

	/** ��־��¼�� */
	private static Logger logger = Logger.getLogger("DirtyWordsBufferLoader");

	/**
	 * ɾ��ָ�������еĹؼ��֡�ѭ��ɾ����ֱ��û�йؼ���Ϊֹ��
	 * 
	 * @param text
	 *            ԭʼ�ı�����
	 * @return ����ɾ���˹ؼ��ֵ��ַ���
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
	 * ɾ��ָ�������еĹؼ��֡�ѭ��ɾ����ֱ��û�йؼ���Ϊֹ��
	 * 
	 * @param text
	 *            ԭʼ�ı�����
	 * @return ����ɾ���˹ؼ��ֵ��ַ���
	 */
	public static String deleteAllWords(String text) {
		try {
			return deleteAllWords(text.toCharArray());
		}
		catch (Exception ex) {
			logger.error("���˹ؼ��ֳ���", ex);
			return text;
		}
	}

	/**
	 * ɾ��ָ�������еĹؼ��֡�ֻɾ��һ�Σ���ѭ��ɾ����
	 * 
	 * @param text
	 *            ԭʼ�ı�����
	 * @param wordsBuffer
	 *            �ؼ��ֻ���
	 * @return ����ɾ���˹ؼ��ֵ��ַ���
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
							logger.info("���˹ؼ���:" + new String(word) + ", text:" + new String(text));
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
	 * ��ʼ������ʵ��
	 * 
	 * @throws Exception
	 */
	public synchronized static void init() throws Exception {
		loadDirtyWordsBuffer();
		new DirtyWordsBufferLoader().start();
	}

	/**
	 * �жϵ�һ���ַ�����ָ��λ���Ƿ��Եڶ����ַ�����ͷ�����Һ�����Ч�ַ���
	 * 
	 * @param cs1
	 *            ��һ���ַ���
	 * @param cs2
	 *            �ڶ����ַ�
	 * @param fromIndex
	 *            ��һ���ַ�����ָ��λ��
	 * @return ���ָ���ַ���ͷ���򷵻�-1�����򷵻�ָ���ַ���ĩβλ�á�
	 */
	private static int isStartWith(char[] cs1, char[] cs2, int fromIndex) {
		if (cs1 == null || cs2 == null) {
			return -1;
		}
		// ʣ�೤��
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
			// ������Ч�ַ�����ת����Сд�����ȫ��
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
				// �����ַ�������
				index1++;
				continue;
			}
			// �����
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
		logger.info("��ʼ��������������");
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
			logger.error("�����ֳ����쳣", ex);
			throw ex;
		}
//		finally {
//			_dba.freeConnection(conn);
//		}
		logger.info("�����ּ������");
	}

}
