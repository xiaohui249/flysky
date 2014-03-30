package com.estool.utils;

import org.elasticsearch.common.text.Text;

/**
 * 把es高亮返回的Text数组转换成字符串
 * 
 **/
public class HighlightUtils {

	public static String textToString(Text[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "";

		StringBuilder b = new StringBuilder();
		for (int i = 0;; i++) {
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.toString();
		}
	}
}
