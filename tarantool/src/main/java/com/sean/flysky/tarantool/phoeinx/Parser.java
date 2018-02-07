package com.sean.flysky.tarantool.phoeinx;

import com.salesforce.phoenix.parse.SQLParser;
import com.salesforce.phoenix.parse.SQLStatement;

import java.io.StringReader;

public class Parser {
	
	public static SQLStatement parse(String sql) throws Exception {
		SQLParser parser = new SQLParser(new StringReader(sql));
		return parser.parseStatement();
	}
}
