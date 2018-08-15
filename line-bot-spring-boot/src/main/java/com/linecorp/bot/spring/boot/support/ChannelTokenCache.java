package com.linecorp.bot.spring.boot.support;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class ChannelTokenCache {
	private Map<String, String> replyTokenTochannelTokenMap = Maps.newConcurrentMap();
	
	public void set(String replyToken, String channelToken) {
		replyTokenTochannelTokenMap.put(replyToken, channelToken);
	}
	
	public String channelToken(String replyToken) {
		return replyTokenTochannelTokenMap.remove(replyToken);
	}
}
