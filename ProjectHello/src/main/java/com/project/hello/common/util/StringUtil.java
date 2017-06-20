package com.project.hello.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class StringUtil {
	private final static String	DEFAULT_UNI_CODESET = "8859_1";
	private final static String	DEFAULT_LOCAL_CODESET = "KSC5601";
	private final static String WHITE_SPACE = " \t\n\r\f";

	private StringUtil() {}

	public static String toString(Object obj)
	{
		if(obj == null)
		{
			return null;
		}
		else if(obj instanceof Vector)
		{
			return toString((Vector)obj);
		}
		else if(obj instanceof Document)
		{
			return toString((Document)obj);
		}
		else
		{
			return obj.toString();
		}
	}

	public static String toString(Vector v)
	{
		if(v == null) return null;
		Object obj = v.firstElement();
		if(obj instanceof Document)
		{
			StringBuffer sb = new StringBuffer("[");
			for ( int i = 0 ; i < v.size(); i++)
			{
				if(i != 0) sb.append(", ");
				sb.append(toString((Document)v.elementAt(i)));
			}
			sb.append("]");
			return sb.toString();
		}
		else
		{
			return v.toString();
		}
	}

	/** String[val1, val2, val3]을 Vector로 convert  **/

	public static Vector convertVector(String val)
	{
		if(val == null) return null;
		if(val.startsWith("["))
		{
			if(val.substring(val.length()-1).equals("]"))
			{
				val = val.substring(1,val.length()-1);
				Vector v = new Vector();
				if(val.startsWith("<?"))
				{
					JStringTokenizer st = new JStringTokenizer(val,">, ");
					while(st.hasMoreTokens())
					{

						String xml = st.nextToken();
						if(!StringUtil.right(xml,1).equals(">"))
						{
							xml += ">";
						}
						try
						{
							v.add(parseXML(xml));
						}
						catch(Exception e)
						{
							System.out.println(xml);
							e.printStackTrace();
						}
					}
				}
				else
				{
					JStringTokenizer st=new JStringTokenizer(val, ", ");
					String oneToken ="" ;
					while(st.hasMoreTokens())
					{
						oneToken = st.nextToken(", ");
						Object obj=oneToken.trim();
						int pos = oneToken.indexOf("{");
						int pos1= oneToken.indexOf("[");
						String delim = null;
						if(pos>pos1)
						{
							delim="}";
						}
						else if(pos < pos1)
						{
							delim = "]";
						}
						if(delim != null)
						{
							oneToken += ", " + st.nextToken(delim + ", ");
							if(st.hasMoreTokens())
							{
								oneToken  += delim;
							}
							if(delim.equals("}"))
							{
								obj = convertHashMap(oneToken);
							}
							else
							{
								obj = convertVector(oneToken);
							}
						}

						v.add(obj);
					}

         	}
				return v;
			}
		}
		return null;
	}

	/** 문자열(str)내에 해당 문자열(repleatStr)이 몇 개 있는지? **/
	public static int getRepeatCount(String str, String repeatStr)
	{
		int cnt = 0;
		int pos = -1;
		while((pos=str.indexOf(repeatStr)) >=0)
		{
			str=str.substring(pos+repeatStr.length());
			cnt++;
		}
		return cnt;
	}

	/** String{key1=val1, key2=val2, key3=val3}을 HashMap으로 convert  **/
	public static HashMap convertHashMap(String val)
	{
		if(val == null) return null;
		int pos = val.indexOf("{");
		HashMap map = null;
		int pos1= val.lastIndexOf("}");
		if(pos1 > pos)
		{
			val = val.substring(pos+1, pos1);
			JStringTokenizer st = new JStringTokenizer(val, ", ");
			map = new HashMap();
			String oneToken ="" ;
			String key = null;
			while(st.hasMoreTokens())
			{
				oneToken = st.nextToken(", ");

				if(oneToken.indexOf("={") >=0)
				{
					int pos2 = oneToken.indexOf("},");
					boolean result = false;
					if(pos2>=0)
					{
						result = right(oneToken,1).equals("}");
					}
					else
					{
						result = true;
					}
					if(result==true)
					{
						oneToken += ", " +  st.nextToken("}, ");
						if(st.hasMoreTokens())
						{
							oneToken  += "}";
						}
					}
				}
				else if(	oneToken.indexOf("=[") >=0)
				{
					int pos2 = oneToken.indexOf("]");
					boolean result = false;
					if(pos2>=0)
					{
						result = right(oneToken,1).equals("]");
					}
					else
					{
						result = true;
					}
					if(result==true)
					{
						oneToken += ", " +  st.nextToken("], ");
						//System.out.println("+++++++++++++++" + oneToken);
						if(st.hasMoreTokens())
						{
							oneToken  += "]";
						}
					}
				}

				/*if(oneToken.indexOf("{") >=0 || oneToken.indexOf("[") >=0)
				{
					if(getRepeatCnt(oneToken,"{") > getRepeatCnt(oneToken,"}") || getRepeatCnt(oneToken,"[") > getRepeatCnt(oneToken,"]")  )
					{
						oneToken += ", ";
						continue;
					}
				}
				*/
				int pos3 = oneToken.indexOf("=");
				if(pos3 >=0 )
				{
					key = oneToken.substring(0,pos3).trim();
					map.put(key, oneToken.substring(pos3+1).trim());
					oneToken = "";
					//System.out.println("put  "  + oneToken.substring(0,pos3) + "," + oneToken.substring(pos3+1));
				}
				else
				{
					if(key != null)
					{
						String newVal = ((String)map.get(key)) +", " + oneToken;
						map.put(key, newVal);
					}
				}
			}
		}
		return map;
	}
	public static Document parseXML(DocumentBuilder builder,String xml) throws  SAXException,IOException
	{

		if(xml == null) return null;
		return builder.parse(new InputSource(new StringReader(xml)));
	}
	public static Document parseXML(String xml) throws   SAXException,IOException,FactoryConfigurationError,ParserConfigurationException
	{
		if(xml == null) return null;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		return parseXML(builder, xml);
	}

	/**
	* Returns a <code>boolean</code> with a value represented by the specified String.
	*
	* The <code>boolean</code> returned represents the value true if the string argument is equal, ignoring case, to the string "true".
	* If the string argument is null, returns the default value argument.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the <code>boolean</code> value represented by the string.
	*/
	public static boolean parseBoolean(String value, boolean defaultValue)
	{
		return (value == null) ? defaultValue : Boolean.valueOf(value).booleanValue();
	}

	/**
	* Returns a <code>boolean</code> with a value represented by the specified String.
	*
	* The <code>boolean</code> returned represents the value true if the string argument is equal, ignoring case, to the string "true".
	* If the string argument is null, return false.
	*
	* @param value a string.
	* @return the <code>boolean</code> value represented by the string.
	*/
	public static boolean parseBoolean(String value)
	{
		return parseBoolean(value, false);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Returns the first character.
	*
	* if the first character is not exist (string argument is null or empty), returns the default value argument.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the first character.
	*/
	public static char parseChar(String value, char defaultValue)
	{
		return (value == null || value.equals("")) ? defaultValue : value.charAt(0);
	}

	/**
	* Returns the first character.
	*
	* If the first character is not exist (string argument is null or empty), return (char) 0.
	*
	* @param value a string.
	* @return the first character.
	*/
	public static char parseChar(String value)
	{
		return parseChar(value, (char) 0);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Parses the string argument as a signed decimal integer.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return default value argument.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the integer represented by the argument in decimal.
	*/
	public static int parseInt(String value, int defaultValue)
	{
		try
		{
			return (value == null || value.equals("") ) ? defaultValue : Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}

	/**
	* Parses the string argument as a signed decimal integer.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return -1.
	*
	* @param value a string.
	* @return the integer represented by the argument in decimal.
	*/
	public static int parseInt(String value)
	{
		return parseInt(value, -1);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Parses the string argument as a signed decimal <code>long</code>.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return default value argument.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the <code>long</code> represented by the argument in decimal.
	*/
	public static long parseLong(String value, long defaultValue)
	{
		try
		{
			return (value == null || value.equals("") ) ? defaultValue : Long.parseLong(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	public static Long parseLong(String value, Long defaultValue)
	{
		try
		{
			return (value == null || "".equals(value) ) ? defaultValue : Long.valueOf(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}

	/**
	* Parses the string argument as a signed decimal <code>long</code>.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return -1L.
	*
	* @param value a string.
	* @return the <code>long</code> represented by the argument in decimal.
	*/
	public static long parseLong(String value)
	{
		return parseLong(value, -1L);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Returns a new <code>float</code> initialized to the value represented by the specified <code>String</code>.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return default value argument.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the <code>float</code> represented by the argument.
	*/
	public static float parseFloat(String value, float defaultValue)
	{
		try
		{
			return (value == null || value.equals("") ) ? defaultValue : Float.parseFloat(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}

	/**
	* Returns a new <code>float</code> initialized to the value represented by the specified <code>String</code>.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return -1.0F.
	*
	* @param value a string.
	* @return the <code>float</code> represented by the argument.
	*/
	public static float parseFloat(String value)
	{
		return parseFloat(value, -1.0F);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Returns a new <code>double</code> initialized to the value represented by the specified <code>String</code>.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return default value argument.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the <code>double</code> represented by the argument.
	*/
	public static double parseDouble(String value, double defaultValue)
	{
		try
		{
			return (value == null || value.equals("") ) ? defaultValue : Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}

	/**
	* Returns a new <code>double</code> initialized to the value represented by the specified <code>String</code>.
	*
	* If the string argument is null or empty or not a number(<code>NumberFormatException<code> occur),
	* return -1.0.
	*
	* @param value a string.
	* @return the <code>double</code> represented by the argument.
	*/
	public static double parseDouble(String value)
	{
		return parseDouble(value, -1.0);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	public static String evl(Object obj) {
		return evl(obj,"");
	}
	
	/**
	* If the value argument value is null or empty, returns defaultValue argument value;
	* if the value argument value is not null and not empty, returns value argument value.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the string argument or default value argument.
	* @see #nvl(String value, String defaultValue)
	*/
	public static String evl(Object obj, String def) {
		return ( obj == null || "".equals(obj) ) ? def : String.valueOf(obj);
	}

	/**
	* If the value argument value is null, returns "";
	* if the value argument value is not null, returns value argument value.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the string argument or default value argument.
	* @see #evl(String value, String defaultValue)
	*/
	public static String nvl(String value)
	{
		return nvl( value , "" );
	}
	
	/**
	* 인자값이 null 이라면 new String[0] 리턴
	* 인자배열값중 null 이 포함된 값이 있다면 null 을 "" 으로 치환하여 리턴
	*
	* @param value a string[].
	* @param defaultValue default value.
	* @return the string argument or default value argument.
	*/
	public static String[] nvls(String[] values){
		
		return nvls(values,"");
	}

	/**
	* If the value argument value is null, returns defaultValue argument value;
	* if the value argument value is not null, returns value argument value.
	*
	* @param value a string.
	* @param defaultValue default value.
	* @return the string argument or default value argument.
	* @see #evl(String value, String defaultValue)
	*/
	public static String nvl(String value, String defaultValue)
	{
		return ( value == null ) ? defaultValue : value;
	}
	/**
	* 인자값이 null 이라면 new String[0] 리턴
	* 인자배열값중 null 이 포함된 값이 있다면 null 을 [defaultValue] 으로 치환하여 리턴
	*
	* @param value a string[].
	* @param defaultValue default value.
	* @return the string[] argument or default value argument.
	*/
	public static String[] nvls(String[] values, String defaultValue){
		
		if(values == null) return new String[0];
		
		String[] str = new String[values.length];
		
		for(int i=0; i<str.length; i++){
			str[i] = nvl(values[i],defaultValue);
		}
		return str;
	}
	
	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Searches for the first occurence of the given element argument, beginning the search at index,
	* and testing for equality using the equals method.
	*
	* @param arr <code>java.lang.Object</code> Array
	* @param element an object.
	* @param index the non-negative index to start searching from.
	* @return the index of the first occurrence of the element argument in the object array argument at position index.
	* returns -1 if the element object is not found in the object array argument.
	*/
	public static int indexOf(Object[] arr, Object element, int index)
	{
		if (arr == null) return -1;

		for (int i=index; i<arr.length; i++)
		{
			if (arr[i] == null)
			{
				if (element==null) return i;
			}
			else
			{
				if (element==null) continue;
				if (arr[i].equals(element)) return i;
			}
		}

		return -1;
	}

	/**
	* Searches for the first occurence of the given element argument, testing for equality using the equals method.
	*
	* @param arr <code>java.lang.Object</code> Array
	* @param element an object.
	* @return the index of the first occurrence of the argument in the object array,
	* returns -1 if the element object is not found in the object array argument.
	*/
	public static int indexOf(Object[] arr, Object element)
	{
		return indexOf(arr, element, 0);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Searches backwards for the specified object, starting from the specified index, and returns an index to it.
	*
	* @param arr <code>java.lang.Object</code> Array
	* @param element an object.
	* @param index the index to start searching from.
	* @return the index of the last occurrence of the specified object in the object array argument at position less than
	* or equal to index in the object array.
	* returns -1 if the element object is not found in the object array argument.
	*/
	public static int lastIndexOf(Object[] arr, Object element, int index)
	{
		if (arr == null) return -1;

		for (int i=arr.length-index; i>=0; i--)
		{
			if (arr[i] == null)
			{
				if (element==null) return i;
			}
			else
			{
				if (element==null) continue;
				if (arr[i].equals(element)) return i;
			}
		}

		return -1;
	}

	/**
	* Returns the index of the last occurrence of the specified object in the object array argument.
	*
	* @param arr <code>java.lang.Object</code> Array
	* @param element an object.
	* @return the index of the last occurrence of the specified object in this the object array argument;
	* returns -1 if the element object is not found.
	*/
	public static int lastIndexOf(Object[] arr, Object element)
	{
		return lastIndexOf(arr, element, 1);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Removes white space from both ends of the string argument.
	*
	* If the argument string is null, returns empty string (&quot;&quot;).<br>
	* <caption>white space</caption>
	* <table>
	* <tr><td>'\t'</td>            <td>&#92;u0009</td>
	*     <td><code>HORIZONTAL TABULATION</code></td></tr>
	* <tr><td>'\n'</td>            <td>&#92;u000A</td>
	*     <td><code>NEW LINE</code></td></tr>
	* <tr><td>'\f'</td>            <td>&#92;u000C</td>
	*     <td><code>FORM FEED</code></td></tr>
	* <tr><td>'\r'</td>            <td>&#92;u000D</td>
	*     <td><code>CARRIAGE RETURN</code></td></tr>
	* <tr><td>'&nbsp;&nbsp;'</td>  <td>&#92;u0020</td>
	*     <td><code>SPACE</code></td></tr>
	* </table>
	*
	* @param value a string.
	* @return a trimed string, with white space removed from the front and end.
	* @see java.lang.String#trim().
	* @see #ltrim(String value).
	* @see #rtrim(String value).
	*/
	public static String trim(String value)
	{
		return (value == null) ? "" : value.trim();
	}

	/**
	* Removes white space from left ends of the string argument.
	*
	* If the argument string is null, returns empty string (&quot;&quot;).
	*
	* @param value a string.
	* @return a left trimed string, with white space removed from the front.
	* @see java.lang.String#trim().
	* @see #trim(String value).
	* @see #rtrim(String value).
	*/
	public static String ltrim(String value)
	{
		if (value == null) return "";

		for (int i=0; i<value.length(); i++)
		{
			if ( WHITE_SPACE.indexOf( value.charAt(i) ) == -1 )
				return value.substring(i);
		}

		return "";
	}

	/**
	* Removes white space from right ends of the string argument.
	*
	* If the argument string is null, returns empty string (&quot;&quot;).
	*
	* @param value a string.
	* @return a right trimed string, with white space removed from the end.
	* @see java.lang.String#trim().
	* @see #trim(String value).
	* @see #ltrim(String value)
	*/
	public static String rtrim(String value)
	{
		if ( value == null || value.equals("") ) return "";

		for (int i=value.length()-1; i>=0; i--)
		{
			if ( WHITE_SPACE.indexOf( value.charAt(i) ) == -1 )
				return value.substring(0, i+1);
		}

		return "";
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Returns value argument,
	* left-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* If the value argument is null, value argument think of empty string (&quot;&quot;).
	*
	* @param value a string value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return left padded string.
	*/
	public static String lpad(String value, int padLen, char padChar)
	{
		if (value == null) value = "";

		while (value.length() < padLen)
		{
			value = padChar + value;
		}

		return value;
	}

	/**
	* Returns value argument,
	* left-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a short value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return left padded string.
	*/
	public static String lpad(short value, int padLen, char padChar)
	{
		return lpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* left-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a int value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return left padded string.
	*/
	public static String lpad(int value, int padLen, char padChar)
	{
		return lpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* left-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a long value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return left padded string.
	*/
	public static String lpad(long value, int padLen, char padChar)
	{
		return lpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* left-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a float value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return left padded string.
	*/
	public static String lpad(float value, int padLen, char padChar)
	{
		return lpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* left-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a double value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return left padded string.
	*/
	public static String lpad(double value, int padLen, char padChar)
	{
		return lpad( String.valueOf(value), padLen, padChar );
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Returns value argument,
	* right-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* If the value argument is null, value argument think of empty string (&quot;&quot;).
	*
	* @param value a string value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return right padded string.
	*/
	public static String rpad(String value, int padLen, char padChar)
	{
		if (value == null) value = "";

		while (value.length() < padLen)
		{
			value = value + padChar;
		}

		return value;
	}

	/**
	* Returns value argument,
	* right-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a short value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return right padded string.
	*/
	public static String rpad(short value, int padLen, char padChar)
	{
		return rpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* right-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a int value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return right padded string.
	*/
	public static String rpad(int value, int padLen, char padChar)
	{
		return rpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* right-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a long value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return right padded string.
	*/
	public static String rpad(long value, int padLen, char padChar)
	{
		return rpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* right-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a float value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return right padded string.
	*/
	public static String rpad(float value, int padLen, char padChar)
	{
		return rpad( String.valueOf(value), padLen, padChar );
	}

	/**
	* Returns value argument,
	* right-padded to length padLen argument with the sequence of character in padChar argument.
	*
	* @param value a double value.
	* @param padLen the total length of the return value.
	* @param padChar padded character.
	* @return right padded string.
	*/
	public static String rpad(double value, int padLen, char padChar)
	{
		return rpad( String.valueOf(value), padLen, padChar );
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Formats a number to the specific formatter stirng.
	*
	* @param no a number string.
	* @param formatter number formatter string.
	* @return formatted number string.
	* @exception NumberFormatException if the no argument is not a number.
	*/
	public static String format(String no, String formatter)
	{
		return format(Double.parseDouble(no), formatter);
	}

	/**
	* Formats a number to the specific formatter stirng.
	*
	* @param no a int number.
	* @param formatter number formatter string.
	* @return formatted number string.
	*/
	public static String format(int no, String formatter)
	{
		return format((long) no, formatter);
	}

	/**
	* Formats a number to the specific formatter stirng.
	*
	* @param no a float number.
	* @param formatter number formatter string.
	* @return formatted number string.
	*/
	public static String format(float no, String formatter)
	{
		return format((double) no, formatter);
	}

	/**
	* Formats a number to the specific formatter stirng.
	*
	* @param no a long number.
	* @param formatter number formatter string.
	* @return formatted number string.
	*/
	public static String format(long no, String formatter)
	{
		DecimalFormat df = new DecimalFormat(formatter);
		return df.format(no);
	}

	/**
	* Formats a number to the specific formatter stirng.
	*
	* @param no a double number.
	* @param formatter number formatter string.
	* @return formatted number string.
	*/
	public static String format(double no, String formatter)
	{
		DecimalFormat df = new DecimalFormat(formatter);
		return df.format(no);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Split a special character separated string, and returns a <code>String</code> array.
	*
	* @param str special character separated string
	* @param delim delimeter character.
	* @param isSkipNull
	* @return splitted string array.
	*/
	public static String[] split(String str, char delim, boolean isSkipNull)
	{
		if (str == null) return null;

		String[] arr = null;
		String strDelim = String.valueOf(delim);

		if (isSkipNull)
		{
			StringTokenizer st = new StringTokenizer( str, strDelim );
			arr = new String[ st.countTokens() ];
			for (int i=0; i<arr.length && st.hasMoreTokens(); i++)
			{
				arr[i] = st.nextToken();
			}
		}
		else
		{
			Vector vt = new Vector();
			boolean setNull = str.startsWith(strDelim);
			StringTokenizer st = new StringTokenizer( str, strDelim, true );
			while ( st.hasMoreTokens() )
			{
				String value = st.nextToken();
				if ( strDelim.equals(value) )
				{
					if (setNull)
						vt.add((String) null);
					else
						setNull = true;
				}
				else
				{
					vt.add(value);
					setNull = false;
				}
			}

			if (setNull)	//if (str.endsWith(strDelim))
				vt.add((String) null);

			if (vt.size() > 0)
			{
				arr = new String[vt.size()];
				vt.copyInto(arr);
			}
		}

		return arr;
	}

	/**
	* Split a comma separated string, and returns a <code>String</code> array.
	*
	* @param str comma separated string
	* @param isSkipNull
	* @return splitted string array.
	*/
	public static String[] split(String str, boolean isSkipNull)
	{
		return split(str, ',', isSkipNull);
	}

	/**
	* Split a special character separated string, and returns a <code>String</code> array.
	*
	* @param str special character separated string
	* @param delim delimeter character.
	* @return splitted string array.
	*/
	public static String[] split(String str, char delim)
	{
		return split(str, delim, true);
	}

	/**
	* Split a comma separated string, and returns a <code>String</code> array.
	*
	* @param str comma separated string
	* @return splitted string array.
	*/
	public static String[] split(String str)
	{
		return split(str, ',', true);
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Join a string array element with a special character.
	*
	* @param arr string array
	* @param delim delimeter character
	* @param isSkipNull
	* @return joined string
	*/
	public static String join(String[] arr, char delim, boolean isSkipNull)
	{
		if (arr == null || arr.length == 0) return null;

		int intCnt = 0;
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<arr.length; i++)
		{
			if (isSkipNull)
			{
				if (arr[i] != null)
				{
					if (intCnt > 0) sb.append(delim);
					sb.append(arr[i]);
					intCnt++;
				}
			}
			else
			{
				if (i > 0) sb.append(delim);
				sb.append(arr[i] == null ? "" : arr[i]);
			}
		}

		return sb.toString();
	}

	/**
	* Join a string array element with a comma.
	*
	* @param arr string array
	* @param isSkipNull
	* @return joined string
	*/
	public static String join(String[] arr, boolean isSkipNull)
	{
		return join(arr, ',', isSkipNull);
	}

	/**
	* Join a string array element with a special character.
	*
	* @param arr string array
	* @param delim delimeter character
	* @return joined string
	*/
	public static String join(String[] arr, char delim)
	{
		return join(arr, delim, true);
	}

	/**
	* Join a string array element with a comma.
	*
	* @param arr string array
	* @return joined string
	*/
	public static String join(String[] arr)
	{
		return join(arr, ',', true);
	}
	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	public static Properties splitNameValue(String str, char delim)
	{
		if (str == null) return null;

		Properties pt = new Properties();
		StringTokenizer st = new StringTokenizer( str, String.valueOf(delim) );
		while ( st.hasMoreTokens() )
		{
			String strElement = st.nextToken();
			int index = strElement.indexOf("=");
			if (index == -1) continue;

			try
			{
				pt.setProperty( strElement.substring(0, index), strElement.substring(index+1) );
			}
			catch (StringIndexOutOfBoundsException e)
			{
				pt.setProperty( strElement.substring(0, index), "" );
			}
		}

		return pt.size() == 0 ? null : pt;
	}
	
	public static Properties splitNameValue(String str)
	{
		return splitNameValue(str, ',');
	}
	
	public static String joinNameValue(Properties pt, char delim)
	{
		if (pt == null || pt.size() == 0) return null;

		Enumeration en = pt.propertyNames();
		StringBuffer sb = new StringBuffer();
		int intCnt  = 0;
		while ( en.hasMoreElements() )
		{
			String strKey = (String) en.nextElement();
			if (intCnt > 0) sb.append(delim);
			sb.append(strKey);
			sb.append("=");
			sb.append( pt.getProperty(strKey) );

			intCnt++;
		}

		return sb.toString();
	}
	
	public static String joinNameValue(Properties pt)
	{
		return joinNameValue(pt, ',');
	}

	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Remove a special character from the str argument.
	*
	* @param str a string
	* @param rmChar removed character.
	* @return a string that special character is removed .
	*/
	public static String removeChar(String str, char rmChar)
	{
		if (str==null || str.indexOf(rmChar) == -1) return str;

		StringBuffer sb = new StringBuffer();
		char[] arr = str.toCharArray();
		for (int i=0; i<arr.length; i++)
		{
			if ( arr[i] != rmChar )
				sb.append( arr[i] );
		}

		return sb.toString();
	}

	/**
	* Remove a special characters from the str argument.
	*
	* @param str a string
	* @param rmChar removed characters.
	* @return a string that special characters are removed .
	*/
	public static String removeChar(String str, char[] rmChar)
	{
		if ( str==null || rmChar == null ) return str;

		char[] arr = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<arr.length; i++)
		{
			boolean isAppend = true;
			for (int k=0; k<rmChar.length; k++)
			{
				if ( arr[i] == rmChar[k] )
				{
					isAppend = false;
					break;
				}
			}
			if (isAppend)
				sb.append( arr[i] );
		}

		return sb.toString();
	}


	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
	//▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

	/**
	* Encode a string from the specific encoding name to other encoding name.
	*
	* @param str a source string
	* @param fromEnc source string encoding name
	* @param toEnc target encoding name
	* @return encoded string.
	* @exception UnsupportedEncodingException If the named encoding is not supported.
	*/
	public static String encodeText(String str, String fromEnc, String toEnc) throws UnsupportedEncodingException
	{
		return (str == null) ? null : new String(str.getBytes(fromEnc), toEnc);
	}

	/**
	* convert a HTML special character string.
	* <pre><code>
	* &lt; --&gt; &amp;lt;
	* &gt; --&gt; &amp;gt;
	* &quot; --&gt; &amp;gt;
	* &amp; --&gt; &amp;amp;</code></pre>
	*
	* @param value HTML special character string.
	* @param converted HTML special character string.
	*/
	public static String convertInput(String value)
	{
		if (value == null) return "";

		char[] arr = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<arr.length; i++)
		{
			switch (arr[i])
			{
				case '\"':
					sb.append("&quot;");
					break;

				case '<':
					sb.append("&lt;");
					break;

				case '>':
					sb.append("&gt;");
					break;

				case '&':
					sb.append("&amp;");
					break;

				default :
					sb.append(arr[i]);
			}
		}

		return sb.toString();
	}

	/**
	* convert a HTML special character string.
	* <pre><code>
	* &lt; --&gt; &amp;lt;
	* &gt; --&gt; &amp;gt;
	* &quot; --&gt; &amp;gt;
	* &amp; --&gt; &amp;amp;
	* \r or \n --&gt; &gt;br&lt\n
	* \t --&gt; &amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;
	* ' ' --&gt; &nbsp;</code></pre>
	*
	* @param value HTML special character string.
	* @param converted HTML special character string.
	*/
	public static String convertHtml(String value)
	{
		if (value == null)
			return "";

		char[] arr = value.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i=0; i<arr.length; i++)
		{
			switch (arr[i])
			{
				case '\n':
					sb.append("<br>\n");
					break;

				case '\r':
					break;

				case '\t':
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					break;

				case '\"':
					sb.append("&quot;");
					break;

				case '<':
					sb.append("&lt;");
					break;

				case '>':
					sb.append("&gt;");
					break;

				case ' ':
					sb.append("&nbsp;");
					break;

				case '&':
					sb.append("&amp;");
					break;

				default	:
					sb.append(arr[i]);
			}
		}

		return sb.toString();
	}

	/**
	* convert a special character string.
	* <pre><code>
	* &lt; --&gt; &amp;lt;
	* &gt; --&gt; &amp;gt;
	* &quot; --&gt; &amp;quot;
	* &amp; --&gt; &amp;amp;
	* @param value HTML special character string.
	* @param converted HTML special character string.
	*/
	public static String convertSpecialChar(String value)
	{
		if (value == null)
			return "";

		char[] arr = value.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i=0; i<arr.length; i++)
		{
			switch (arr[i])
			{

				case '\"':
					sb.append("&quot;");
					break;

				case '<':
					sb.append("&lt;");
					break;

				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;

				default	:
					sb.append(arr[i]);
			}
		}

		return sb.toString();
	}

	/**
	* convert a HTML special character string.
	* <pre><code>
	* &quot; --&gt; &amp;gt;
	* &amp; --&gt; &amp;amp;
	* \r or \n --&gt; &gt;br&lt\n
	* \t --&gt; &amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;
	* ' ' --&gt; &nbsp;</code></pre>
	*
	* @param value HTML special character string.
	* @param converted HTML special character string.
	*/
	public static String convertPre(String value)
	{
		if (value == null)
			return "";

		char[] arr = value.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i=0; i<arr.length; i++)
		{
			switch (arr[i])
			{
				case '\n':
					sb.append("<br>\n");
					break;

				case '\r':
					break;

				case '\t':
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					break;

				case '\"':
					sb.append("&quot;");
					break;

				case ' ':
					sb.append("&nbsp;");
					break;

				case '&':
					sb.append("&amp;");
					break;

				default	:
					sb.append(arr[i]);
			}
		}

		return sb.toString();
	}

	/**
	* @param double no
	* @param String
	*/
	public static String makeMoneyType(double no)
	{
		return NumberFormat.getInstance().format(no);
	}
	/**
	* @param float no
	* @param String
	*/
	public static String makeMoneyType(float no)
	{
		return ( makeMoneyType( (new Float(no)).doubleValue() ) );
	}
	/**
	* @param String no
	* @param String
	*/
	public static String makeMoneyType(int no)
	{
		return ( makeMoneyType( (new Integer(no)).longValue() ) );
	}
	/**
	* @param long no
	* @param String
	*/
	public static String makeMoneyType(long no)
	{
		return NumberFormat.getInstance().format(no);
	}
	/**
	 * @param Long no
	 * @param String
	 */
	public static String makeMoneyType(Long no)
	{
		if( no == null ) return "-";
		return NumberFormat.getInstance().format(no);
	}
	/**
	* 스트링으로 넘겨 받은 문자를 123,333형태로 변환
	* @param String no
	* @param String
	*/
	public static String makeMoneyType(String no) {
		if( no == null ) return "";
		for ( int i=0; i< no.length(); i++)
		{
			if(no.charAt(i)=='0')
			{
				continue;
			}
			else
			{
				no = no.substring(i);
				break;
			}
		}
		int index = no.indexOf(".");
		if (index == -1)
		{
			return makeMoneyType( Long.parseLong(no) );
		}
		else
		{
			return (
				makeMoneyType( Long.parseLong(no.substring(0, index)) ) +
				no.substring(index, no.length())
			);
		}
	}

	/**
	 * strSrc String을 받아 strOld String을 찾아 strNew String으로 바꿔준다.
	 * 작성 날짜: (2001-03-24 오후 2:21:10)
	 * @return java.lang.String
	 * @param strSrc,strOld,strNew java.lang.String
	 */
	public static String replace(String originString,String srchString,String repString){
		if(originString == null) return null;
		if(originString.equals("")) return "";
		if(srchString == null || srchString.equals("")) return originString;

		String result = "";
		int i;

		do
		{
			i = originString.indexOf(srchString);

			if(i != -1)
			{
				result += originString.substring(0, i);
				result += repString;
				originString = originString.substring(i + srchString.length());
			}
			else
			{
				result += originString;
				break;
			}
		}
		while(i != -1);

		return result;
	}
	
	public static String replaceIgnoreCase(String originString,String srchString,String repString){
		if(originString == null) return null;
		if(originString.equals("")) return "";
		if(srchString == null || srchString.equals("")) return originString;
		String result = "";
		int i;

		do
		{
			i = originString.toLowerCase().indexOf(srchString.toLowerCase());

			if(i != -1)
			{
				result += originString.substring(0, i);
				result += repString;
				originString = originString.substring(i + srchString.length());
			}
			else
			{
				result += originString;
				break;
			}
		}
		while(i != -1);

		return result;
	}
	
	/** return str.substring(0,endIdx) **/
	public static String left(String str, int len)
	{
		return str.substring(0,len);
	}
	
	/** return str.substring(str.length() - len) **/
	public static String right(String str, int len)
	{
		return str.substring(str.length() - len);
	}
	
	public static String getInnerString(String str, String start, String last)
	{
		int pos = str.indexOf(start);
		if  ( pos>=0)
		{
			int pos1 = str.indexOf(last, pos+1);
			if ( pos1 > pos)
			{
				return str.substring(pos+start.length(), pos1);
			}
		}
		return "";
	}
	
	public static String getInnerStringIgnoreCase(String str, String start, String last)
	{
		String str1 = str.toUpperCase();
		int pos = str1.indexOf(start.toUpperCase());
		if  ( pos>=0)
		{
			int pos1 = str1.indexOf(last.toUpperCase(), pos+1);
			if ( pos1 > pos)
			{
				return str.substring(pos+start.length(), pos1);
			}
		}
		return "";
	}
	
	/**
	* 확장자가 있는 화일명을 화일명을 삭제하고 확장자만 넘겨줌
	* @param String filename
	* @return String
	*/			
	public static String getFileExtension(String filename)
	{
		if (filename == null) return null;

		int len = filename.length();
		if (len == 0) return filename;

		int last = filename.lastIndexOf(".");
		if (last == -1) return filename;

		return filename.substring( last + 1, len );
	}
	
	/**
	* 공백 치환 메소드(파일 다운로드)
	* @param String str
	* @return String
	*/
	public static String getChangeArray( String str ){
		StringBuffer sb = new StringBuffer();

		if( str == null ) return "";

		for( int i = 0; i < str.length(); i++ ){
			if( str.charAt( i ) != '+' ){
				sb.append( str.charAt( i ) );
			}else {
				sb.append( "%20" );
			}
		}

		return sb.toString();
	}

	/**
	* String을 truncLen길이만큼만 남겨놓고 뒤를 ...으로 붙여줌(가나다라마 -> 가나다...)
	* @param String str
	* @param String truncLen
	* @return String
	*/	
	public static String truncateString(String str, int truncLen)
	{
		return truncateString(str, truncLen, "...");
	}
	
	/**
	* String을 truncLen길이만큼만 남겨놓고 뒤를 lastStr로 붙여줌.  
	* @param String str
	* @param String truncLen
	* @return String
	*/		
	public static String truncateString(String str, int truncLen, String lastStr)
	{
		if (str == null)
			return str;

		int len = str.length();
		if (len <= truncLen)
			return str;

		return str.substring(0, truncLen) + ((lastStr == null) ? "" : lastStr);
	}

	/**
	 * 문자열을 limit 만큼 남겨 놓고 뒤를 lastStr로 붙여줌(한글도 작동)
	 * @param String str
	 * @param int limit
	 * @return String
	 */
	public static String cutCharLen(String str, int limit)
	{
		return cutCharLen(str, limit, "...");
	}

	/**
	 * 문자열을 limit 만큼 남겨 놓고 뒤를 lastStr로 붙여줌(한글도 작동)
	 * @param String str
	 * @param int limit
	 * @param String lastStr
	 * @return String
	 */
	public static String cutCharLen(String str, int limit, String lastStr) {
		if (str == null || limit < 4)
			return str;

		int len = str.length();
		int cnt=0, index=0;

		while (index < len && cnt < limit) {
			if (str.charAt(index++) < 256) // 1바이트 문자라면...
				cnt++; // 길이 1 증가
			else { // 2바이트 문자라면...
				if(cnt < limit-3) {
					cnt += 2; // 길이 2 증가
				} else {
					break;
				}
			}
		}

		if (index < len)
			str = str.substring(0, index) + (lastStr==null ? "" : lastStr);

		return str;
	}
    
    /**
	 * String을 byetLength 바이트 길이 만큼만 남겨 놓고 뒤에 ...을 붙인다.
	 * @param str
	 * @param byetLength
	 * @return
	 */
	public static String getByteStringCut(String str, int byetLength){
		return getByteStringCut(str, byetLength, "...");
	}
	
	/**
	 * String을 byetLength 바이트 길이 만큼만 남겨 놓고 뒤에 lastStr를 붙여준다.
	 * @param str
	 * @param byetLength
	 * @param lastStr
	 * @return
	 */
	public static String getByteStringCut(String str, int byetLength, String lastStr){
		
		if(byetLength <= 0) return str;
		if(str == null) return str;
		
		byte[] bytes = str.getBytes();
		
		int len = bytes.length;
		int counter = 0;
		
		if(byetLength >= len){
			return str;
		}
		
		for (int i = byetLength - 1; i >= 0; i--) {
			if (((int)bytes[i] & 0x80) != 0) counter++;
		}
		
		return new String(bytes, 0, byetLength - (counter % 2))+lastStr;
		
	}
		
	/**
	* Enter값 <br>값으로 인식하기
	*/
	public static String setBR(String asomething)  
	{
		int i,len = asomething.length();
     	StringBuffer dest = new StringBuffer(len+500);
     	for( i = 0 ; i < len ; i++)
     	{
        	if( asomething.charAt(i) == '\n')
            		dest.append("<br>");
        	else
            		dest.append(asomething.charAt(i));
    	}
    		return dest.toString();
	}

	/**
	* HTML 태그를 가지고 있는 경우 특수문자로 변경하여 전체 레이아웃이 깨지지 않게 함.
	* @param  String str
	* @return String
	*/		
    public static String escapeHTML(String str){ 
    
        if(null==str) return str; 
    
        char c = ' ';
    
        StringBuffer sb=new StringBuffer(str.length()); 
    
        for(int i=0;i<str.length();i++){ 
            c=str.charAt(i); 
            switch (c){
                case '&'    :   sb.append("&amp;");     break;
                case '"'    :   sb.append("&quot;");    break;
                case '<'    :   sb.append("&lt;");      break;
                case '>'    :   sb.append("&gt;");      break;
                case '\''   :   sb.append("&#39;");     break;
                case ' '    :   sb.append("&nbsp;");    break;
                default     :   sb.append(c); 
            }
        } 
    
        return sb.toString(); 
    }
    
	/**
	* 해당 String 값이 숫자값이면 true, 아니면 false 리턴
	* @param  String str
	* @return boolean
	*/    
    public static boolean isNumberic(String str){
    	return Pattern.matches("[0-9]+", str);
    	
    }
	
	/**
	* 해당 String 값이 숫자값이면 parseInt한 값을 , 아니면 -1 리턴
	* @param  String str
	* @return int
	*/    
    public static int isNumberic2(String str){
    	if(Pattern.matches("[0-9]+", str))
    		return parseInt(str);
    	else
    		return -1;
    }    

	/**
	* 원본 스트링을 시작위치부터 끝까지 잘라준다. 
	* substring(원본, 시작위치)
	* @param  원본		String
	* @param  시작위치	int
	* @return String
	*/    
    public static String substring(String str, int startPos){
    	if(str==null) return "";
    	
    	int strLen = str.length();
		if(startPos < 0 || strLen <= startPos)	return "";
		
		return str.substring(startPos);
    }
    
	/**
	* 원본 스트링을 시작위치부터 끝위치까지 잘라준다. 
	* substring(원본, 시작위치, 끝위치)
	* @param  원본		String
	* @param  시작위치	int
	* @param  끝위치	int
	* @return String
	*/    
    public static String substring(String str, int startPos, int endPos){
    	if(str==null) return "";
    	
    	int strLen = str.length();
		if(startPos < 0 || strLen <= startPos)	return "";

		if(strLen < endPos)	return "";

		if(startPos > endPos)	return "";

   		return str.substring(startPos, endPos);
    }  

	/**
	* 원본 스트링을 시작위치부터 끝까지 잘라준다.(substring 메소드 보완한다.)
	* substring(원본, 시작위치)
	* @param  원본		String
	* @param  시작위치	int
	* @return String
	* @see	  String data="123456789"; substr(a, 2);	//"3456789"
	* @see	  String data="123456789"; substr(a, -2);	//"89"
	*/    
    public static String substr(String str, int startPos){
    	if(str==null) return "";
    	
    	int strLen = str.length();
		if(strLen <= startPos)	return "";

		if(startPos < 0 && startPos < -strLen)	return "";

		if(startPos < 0)
			return str.substring(strLen+startPos);
		else
			return str.substring(startPos);
    }
    
	/**
	* 원본 스트링을 시작위치부터 길이만큼 잘라준다.(substring 메소드 보완한다.)
	* substring(원본, 시작위치, 길이)
	* @param  원본		String
	* @param  시작위치	int
	* @param  끝위치		int
	* @return String
	* @see	  String data="123456789"; substr(a, 2, 5);		//"34567"
	* @see	  String data="123456789"; substr(a, -2, 1);	//"8"
	* @see	  String data="123456789"; substr(a, 1, -1);	//"2345678"
	* @see	  String data="123456789"; substr(a, -5, -1);	//"5678"
	*/    
    public static String substr(String str, int startPos, int length){
    	if(str==null) return "";
    	
    	int strLen = str.length();
		if(strLen <= startPos)	return "";

		if(startPos < 0 && startPos < -strLen)	return "";
		if(length == 0)	return "";
		if(length < 0 && length <= -strLen)	return "";

		if(startPos < 0)	startPos = strLen+startPos;		// -값 계산
		int endPos = 0;
		if(length < 0)	
			endPos = strLen+length;							// -값 계산
		else
			endPos = startPos+length;	
		if(endPos > strLen)	endPos = strLen;
		if(startPos >= endPos)	return "";

   		return str.substring(startPos, endPos);
    }
    
    /**
     * 알파벳과 숫자를 무작위로 섞어 임의의 문자열을 기본 6자리로 발행한다.
     * 
     * @return
     */
    public static String getRandomString() {
    	return getRandomString(6);
    }
    
	/**
	 * 알파벳과 숫자를 무작위로 섞어 임의의 문자열을 지정한 길이로 발행한다.
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		
		Random rn = new Random();
		StringBuffer sb = new StringBuffer();

		String[] alphabet = { "A","B","C","D","E","F","G","H","I","J","K","L","M","N",
		                      "O","P","Q","R","S","T","U","V","W","X","Y","Z" };
		String[] numeric  = {"0","1","2","3","4","5","6","7","8","9" };
		
		for(int i=0;i<length;i++) {
			int selArray = rn.nextInt(2);
			if(selArray == 0) {
				int idx = rn.nextInt(alphabet.length);
				sb.append(alphabet[idx]);
			}else{
				int idx2 = rn.nextInt(numeric.length);
				sb.append(numeric[idx2]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 숫자를 무작위로 섞어 6바이트 문자열 형태로 반환
	 * @return
	 */
	public static String getRandomNumber() {
		return getRandomNumber(6);
	}
	
	/**
	 * 숫자를 무작위로 섞어 지정한 길이만큼 문자열 형태로 반환
	 * @param length
	 * @return
	 */
	public static String getRandomNumber(int length) {
		
		Random rn = new Random();
		StringBuffer sb = new StringBuffer();
		
		String[] numeric  = {"0","1","2","3","4","5","6","7","8","9" };
		
		int idx = 0;
		
		for (int i = 0; i < length; i++) {
			idx = rn.nextInt(numeric.length);
			sb.append(numeric[idx]);
		}
		return sb.toString();
	}

    /**
     * 대소문자를 상관하지 않고 str 문자열에 포함된 keyword 를 찾아서 원래의 문자에 붉은색 폰트태그를 삽입한 문자열 반환
     * <p>
     * added by pwjman
     * </p>
     * @param str
     * @param keyword
     * @return
     */
    public static String markText(String str, String keyword) {
        return markText(str, keyword, "color:red");
    }

    /**
     * 대소문자를 상관하지 않고 str 문자열에 포함된 keyword 를 찾아서 css style을 적용하여 문자열 반환
     * <p>
     * by kenu at http://okjsp.pe.kr
     * </p>
     * @param str
     * @param keyword
     * @param style
     * @return
     */
    public static String markText(String str, String keyword, String style) {
        keyword = replace(replace(replace(keyword, "[", "\\["), ")", "\\)"),
                "(", "\\(");

        Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        int start = 0;
        int lastEnd = 0;
        StringBuffer sbuf = new StringBuffer();
        while (m.find()) {
            start = m.start();
            sbuf.append(str.substring(lastEnd, start)).append("<FONT style='").append(style).append("'>").append(
                    m.group()).append("</FONT>");
            lastEnd = m.end();
        }
        return sbuf.append(str.substring(lastEnd)).toString();
    }
	
	/**
	 * [오퍼레이션명] HashMap print<br>
	 * [요약] <br>
	 *
	 * @param hm
	 */
    public static void printHashMap(HashMap<String, ?> hm) {
		if ( hm != null ) {
			Set<String> set = hm.keySet();
			Object[] key = set.toArray();
			
			for (int i=0;i<key.length;i++) {
				if ( hm.containsKey(key[i]) )
					System.out.println("key["+key[i]+"]="+"value["+hm.get(key[i])+"]");
			}
		}
	}
    
    /**
     * [오퍼레이션명] request를 String형식으로 반환<br>
     * [요약] request를 String형식으로 반환<br>
     *
     * @param request
     * @return
     */
    public static String requestToString( HttpServletRequest request ){
    	String param = "";
    	try {
    		StringBuffer paramSb = new StringBuffer();
    		Enumeration<String> enum2 = request.getParameterNames();
    		while(enum2.hasMoreElements()) {
    			String key = (String)enum2.nextElement();
    			paramSb.append(key+"="+request.getParameter(key)+"&");
    		}
    		param = paramSb.substring(0,paramSb.lastIndexOf("&"));
    	} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return param;
    }
    
    /**
     * [오퍼레이션명] 파일명 언어 처리<br>
     * [요약] <br>
     *
     * @param fileName
     * @return
     */
    public static String getConvertCodeSet(String fileName) {
    	if ( fileName == null ) return null;
    	
    	String str = System.getProperty("os.name").toLowerCase();
    	try {
	    	if ( str.charAt(0) == 'w' )
	    		fileName = new String(fileName.getBytes(), DEFAULT_UNI_CODESET);
	    	else if ( str.charAt(0) == 'l' )
	    		fileName = new String(fileName.getBytes(DEFAULT_LOCAL_CODESET), DEFAULT_UNI_CODESET);
	    	else
	    		fileName = new String(fileName.getBytes(), "EUC-KR");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return fileName;
    }
}