package com.project.hello.common.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class JStringTokenizer implements Enumeration {
	private int m_currentPosition;

	private int m_maxPosition;

	private String m_str;

	private String m_delimiter;

	private boolean m_retTokens;

	private boolean m_isToken = false;

	private boolean m_isNullSkip;

	/**
	 * Constructs a string tokenizer for the specified string. The tokenizer
	 * uses the default delimiter set, which is
	 * <code>"&#92;t&#92;n&#92;r&#92;f"</code>: the space character, the tab
	 * character, the newline character, the carriage-return character, and the
	 * form-feed character. Delimiter characters themselves will not be treated
	 * as tokens.
	 * 
	 * @param str
	 *            a string to be parsed.
	 */
	public JStringTokenizer(String str) {
		this(str, " ", false, true);
	}

	/**
	 * Constructs a string tokenizer for the specified string. The characters in
	 * the <code>delim</code> argument are the delimiters for separating
	 * tokens. Delimiter characters themselves will not be treated as tokens.
	 * 
	 * @param str
	 *            a string to be parsed.
	 * @param delim
	 *            the delimiters.
	 */
	public JStringTokenizer(String str, String delim) {
		this(str, delim, false, true);
	}

	/**
	 * Constructs a string tokenizer for the specified string. All characters in
	 * the <code>delim</code> argument are the delimiters for separating
	 * tokens.
	 * <p>
	 * If the <code>returnTokens</code> flag is <code>true</code>, then the
	 * delimiter characters are also returned as tokens. Each delimiter is
	 * returned as a string of length one. If the flag is <code>false</code>,
	 * the delimiter characters are skipped and only serve as separators between
	 * tokens.
	 * 
	 * @param str
	 *            a string to be parsed.
	 * @param delim
	 *            the delimiters.
	 * @param returnTokens
	 *            flag indicating whether to return the delimiters as tokens.
	 */
	public JStringTokenizer(String str, String delim, boolean returnTokens) {
		this(str, delim, returnTokens, true);
	}

	/**
	 * @param str
	 *            a string to be parsed.
	 * @param delim
	 *            the delimiters.
	 * @param returnTokens
	 *            flag indicating whether to return the delimiters as tokens.
	 * @param isNullSkip
	 *            flag indicating whether to skip if tokens is null
	 * 
	 */
	public JStringTokenizer(String str, String delim, boolean returnTokens, boolean isNullSkip) {
		m_currentPosition = 0;
		m_str = str;
		m_maxPosition = str.length();
		m_delimiter = delim;
		m_retTokens = returnTokens;
		m_isNullSkip = isNullSkip;
	}

	/**
	 * Calculates the number of times that this tokenizer's
	 * <code>nextToken</code> method can be called before it generates an
	 * exception. The current position is not advanced.
	 * 
	 * @return the number of tokens remaining in the string using the current
	 *         delimiter set.
	 * @see java.util.Tokenizer#nextToken()
	 */
	public int countTokens() {
		int count = 0;
		int currpos = m_currentPosition;
		while (true) {
			currpos = m_str.indexOf(m_delimiter, currpos);
			if (currpos >= 0) {
				count++;
				currpos++;
			} else {
				break;
			}
		}

		return count;
	}

	/**
	 * Returns the same value as the <code>hasMoreTokens</code> method. It
	 * exists so that this class can implement the <code>Enumeration</code>
	 * interface.
	 * 
	 * @return <code>true</code> if there are more tokens; <code>false</code>
	 *         otherwise.
	 * @see java.util.Enumeration
	 * @see java.util.Tokenizer#hasMoreTokens()
	 */
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	/**
	 * Tests if there are more tokens available from this tokenizer's string. If
	 * this method returns <tt>true</tt>, then a subsequent call to
	 * <tt>nextToken</tt> with no argument will successfully return a token.
	 * 
	 * @return <code>true</code> if and only if there is at least one token in
	 *         the string after the current position; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasMoreTokens() {
		return (m_currentPosition <= m_maxPosition - 1);
	}

	/**
	 * Returns the same value as the <code>nextToken</code> method, except
	 * that its declared return value is <code>Object</code> rather than
	 * <code>String</code>. It exists so that this class can implement the
	 * <code>Enumeration</code> interface.
	 * 
	 * @return the next token in the string.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 * @see java.util.Enumeration
	 * @see java.util.Tokenizer#nextToken()
	 */
	public Object nextElement() {
		return nextToken();
	}

	/**
	 * Returns the next token from this string tokenizer.
	 * 
	 * @return the next token from this string tokenizer.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 */
	public String nextToken() {

		if (m_retTokens == true) {
			if (m_isToken == true) {
				m_isToken = false;
				return m_delimiter;
			} else {
				m_isToken = true;
			}
		}
		if (m_currentPosition > m_maxPosition) {
			throw new NoSuchElementException();
		}

		int start = m_currentPosition;
		int end = m_str.indexOf(m_delimiter, start);
		// System.out.println("....." + start + ":" + end );

		if (end < 0) {
			m_currentPosition = m_maxPosition;
			end = m_currentPosition;
		} else {
			m_currentPosition = end + m_delimiter.length();
		}
		if (start == end) {
			if (m_isNullSkip == true && end < m_maxPosition) {
				return nextToken();
			} else {
				return "";
			}
		} else {
			return m_str.substring(start, end);
		}
	}

	/**
	 * Returns the next token in this string tokenizer's string. First, the set
	 * of characters considered to be delimiters by this <tt>Tokenizer</tt>
	 * object is changed to be the characters in the string <tt>delim</tt>.
	 * Then the next token in the string after the current position is returned.
	 * The current position is advanced beyond the recognized token. The new
	 * delimiter set remains the default after this call.
	 * 
	 * @param delim
	 *            the new delimiters.
	 * @return the next token, after switching to the new delimiter set.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 */
	public String nextToken(String delim) {
		m_delimiter = delim;
		return nextToken();
	}
}