package com.sean.flysky.netty.http;

import java.util.concurrent.ConcurrentHashMap;

public class Configure {
	public static ConcurrentHashMap<String, AbstractAction> container = new ConcurrentHashMap<String, AbstractAction>();
}
