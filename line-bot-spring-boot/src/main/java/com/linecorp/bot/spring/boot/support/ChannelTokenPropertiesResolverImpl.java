package com.linecorp.bot.spring.boot.support;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "line.bot")
public class ChannelTokenPropertiesResolverImpl implements ChannelTokenResolver {
	@Setter
	private Map<String, String> resolverMap = Maps.newHashMap();
	
	@Override
	public String resolve(String handlerPath) {
		return resolverMap.get(handlerPath);
	}
}
