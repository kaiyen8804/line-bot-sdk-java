package com.linecorp.bot.spring.boot.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.spring.boot.LineBotProperties;

@Component
public class LineMessagingClientFactory {
    @Autowired
    private LineBotProperties lineBotProperties;
	private Map<String, LineMessagingClient> lineMessagingClientHolder = Maps.newConcurrentMap();
	
    public LineMessagingClient get(String channelToken) {
        return lineMessagingClientHolder.computeIfAbsent(channelToken, key -> 
        	LineMessagingClient
            .builder(key)
            .apiEndPoint(lineBotProperties.getApiEndPoint())
            .connectTimeout(lineBotProperties.getConnectTimeout())
            .readTimeout(lineBotProperties.getReadTimeout())
            .writeTimeout(lineBotProperties.getWriteTimeout())
            .build()
        );
    }
}
