package com.linecorp.bot.spring.boot.support;

public interface ChannelTokenResolver {
	String resolve(String handlerPath);
}
