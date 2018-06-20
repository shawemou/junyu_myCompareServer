package com.my.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {

	// / 保证获取到的BeginIndex为可用值(不可用则重置为0)
	public static int BeginIndex(String temp) {
		try {
			Integer.parseInt(temp);
		} catch (Exception e) {
			return 0;
		}
		return Integer.parseInt(temp);

	}

	// / 保证获取到的EndIndex为可用值(不可用则重置为10)
	public static int EndIndex(String temp) {
		try {
			Integer.parseInt(temp);
		} catch (Exception e) {
			return 10;
		}
		return Integer.parseInt(temp);

	}

	// / 保证获取到的字符串不为null(null则重置为"",不为空则UTF-8解码)
	public static String HadStr(String temp)
			throws UnsupportedEncodingException {
		if (temp == null || temp.equals("undefined") || temp.equals("null")
				|| temp.equals("")) {
			temp = "";
		} else {
			temp = URLDecoder.decode(temp, "UTF-8");
		}
		return temp;
	}

	public static boolean IsStringNULL(String temp) {
		if (temp == null || temp.isEmpty() || ("").equals(temp)
				|| temp.equals("undefined") || temp.equals("null"))
			return true;
		return false;
	}

	// / 保证获取到的Datetime为可用值(不可用则为当前时间)
	public static Date IsDatetime(String temp) {
		Date curDate;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			curDate = formatter.parse(temp);// 不推荐直接使用Date.parse();
		} catch (Exception e) {
			e.printStackTrace();
			curDate = new Date();
		}
		formatter = null;
		return curDate;

	}

	// / 保证获取到的Datetime为可用值(不可用则为当前时间)
	public static String IsDatetime2(String temp) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			formatter.parse(temp);// 不推荐直接使用Date.parse();
		} catch (Exception e) {
			e.printStackTrace();
			return formatter.format(new Date());
		}
		formatter = null;
		return temp;

	}

	/**
	 * 多线程中处理不用静态变量不容易出问题(formatter每次都创建)
	 * 
	 * @param temp
	 * @return
	 */
	public static boolean IsDatetime3(String temp) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			formatter.parse(temp);// 不推荐直接使用Date.parse();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		formatter = null;
		return true;
	}

	/**
	 * 获取SimpleDateFormat 格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static DateFormat getDateTimeFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter;
	}
	
	/**
	 * 获取SimpleDateFormat 格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static DateFormat getDateFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter;
	}

	// 保证取到的是数字(不可用返回0)
	public static int IsNum(String temp) {
		try {
			Integer.parseInt(temp);
		} catch (Exception e) {
			return -1;
		}
		return Integer.parseInt(temp);

	}

	// 用JAVA自带的函数检测是否为数字
	public static boolean isNumeric1(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// 用正则表达式检测是否为数字
	public static boolean isNumeric2(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// 用ascii码检测是否为数字
	public static boolean isNumeric3(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	/*
	 * @param value
	 * 
	 * double数据.
	 * 
	 * @param scale
	 * 
	 * 精度位数(保留的小数位数).
	 * 
	 * @param roundingMode
	 * 
	 * 精度取值方式.
	 * 
	 * @return 精度计算后的数据.
	 */

	public static double round(double value, int scale, int roundingMode) {

		BigDecimal bd = new BigDecimal(value);

		bd = bd.setScale(scale, roundingMode);

		double d = bd.doubleValue();

		bd = null;

		return d;

	}

	/**
	 * 将给定的字符串转换为UTF编码的字符串。
	 * 
	 * @param str
	 *            输入字符串
	 * @return
	 * @return 经UTF编码后的字符串，如果有异常，则返回原编码字符串
	 */
	public static String toUTF8(String str) {
		if (str == null || str.isEmpty() || str.equals("")) {
			return str;
		}
		String retVal = str;
		try {
			retVal = new String(str.getBytes("ISO8859_1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	// 去除字符串首尾字符
	public static String trim(String s) {
		int i = s.length();// 字符串最后一个字符的位置
		int j = 0;// 字符串第一个字符
		int k = 0;// 中间变量
		char[] arrayOfChar = s.toCharArray();// 将字符串转换成字符数组
		while ((j < i) && (arrayOfChar[(k + j)] <= ' '))
			++j;// 确定字符串前面的空格数
		while ((j < i) && (arrayOfChar[(k + i - 1)] <= ' '))
			--i;// 确定字符串后面的空格数
		return (((j > 0) || (i < s.length())) ? s.substring(j, i) : s);// 返回去除空格后的字符串
	}

	public static String trimAllToUpper(String s) {
		if (s == null) {
			return s;
		}
		s = s.toUpperCase();// 忽略大小写,统一用大写
		s = s.replaceAll("\\s*", "");// 替换大部分空白字符
		return s;

	}

	/**
	 * java后台验证身份证格式[18位数字或17位数字+x|X或15位数字]
	 * 
	 * @param strPersonId
	 * @return
	 */
	public static boolean IsValidPersonId(String strPersonId) {
		//去掉支持17位数字"\\d{17})(X|x|\\d?)|\\d{15}$"
		Pattern pat = Pattern.compile("^(\\d{17})(X|x|\\d)$|^\\d{15}$");
		Matcher matcher = pat.matcher(strPersonId);
		return matcher.matches();
	}

	/**
	 * java后台验证姓名格式[中文,字母,英文,下划线,中文·号]
	 * 
	 * @param strPersonName
	 * @return
	 */
	public static boolean IsValidPersonName(String strPersonName) {
		Pattern pat = Pattern.compile("^(\\w|[\u4E00-\u9FA5]|\\·)*$");// \w
//		Pattern pat = Pattern.compile("^[\u4e00-\u9fa5]*$");// \w
		// 数字,字母,下划线,\u4E00-\u9FA5
		// 中文
		// 特殊中文字符·间隔号
		Matcher matcher = pat.matcher(strPersonName);
		return matcher.matches();
	}

	/**
	 * 判断数字a的第几位是否为1
	 * 
	 * @param a
	 *            原始数字
	 * @param bit
	 *            第几位 ,从0开始
	 * @return true/false
	 */
	public static boolean isset(int a, int bit) {
		a = a >> bit;
		if ((a & 1) == 0)
			return false;
		return true;
	}
	/**
	 * 根据身份证号码得到性别
	 * 
	 * @param strID
	 * @return true表示男(默认),false表示女
	 */
	public static boolean isMan(String strID) {

		if (Check.IsStringNULL(strID) || strID.length() != 15
				&& strID.length() != 18) {
			return true;
		} else {
			String lastValue = null;
			// 如果是15位,则是最后一位表示性别,偶数为女,奇数为男
			if (strID.length() == 15)
				lastValue = strID.substring(strID.length() - 1, strID.length())
						+ "";
			// 如果是18位,则是倒数第二位表示性别
			if (strID.length() == 18)
				lastValue = strID.substring(strID.length() - 2,
						strID.length() - 1)
						+ "";
			int sex = Check.IsNum(lastValue) % 2;
			return sex == 0 ? false : true;
		}

	}

	/**
	 * 获取List中除<0外的平均值
	 * 
	 * @param arrSimilarity
	 * @return int 平均值
	 */
	public static int getAverage(List<Integer> arrSimilarity) {
		// 遍历相似度List,得到平均相似度(剔除<0的)
		int iTotalSim = 0;
		int iTotalCount = 0;
		int iSimilarity = -100;
		for (int i : arrSimilarity) {
			if (i > 0) {
				iTotalSim += i;
				iTotalCount++;
			}else{
				iSimilarity = i;
			}
		}
		if (iTotalCount > 0)
			iSimilarity = iTotalSim / iTotalCount;
		return iSimilarity;
	}

	/**
	 * 获取List中除<0外的最大值
	 * 
	 * @param arrSimilarity
	 * @return int 最大值
	 */
	public static int getMax(List<Integer> arrSimilarity) {
		// 遍历相似度List,得到最大相似度(剔除<0的)
		int iMaxSim = 0;
		for (int i : arrSimilarity) {
			if (i >= 0 && i <= 1000) {
				if (i < iMaxSim) {
					iMaxSim = i;
				}
			}
		}
		return iMaxSim;
	}

	public static void main(String[] args) {

	}
}
