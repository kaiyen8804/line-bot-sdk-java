package com.linecorp.bot.spring.boot.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class ChannelTokenCache {
	private Cache replyTokenTochannelTokenCache;
	
	@Autowired
	public ChannelTokenCache(CacheManager cacheManager) {
		replyTokenTochannelTokenCache = cacheManager.getCache("channelToken");
	}
	
	public void set(String replyToken, String channelToken) {
		replyTokenTochannelTokenCache.put(replyToken, channelToken);
	}
	
	public String channelToken(String replyToken) {
		return (String) replyTokenTochannelTokenCache.get(replyToken).get();
	}
}
