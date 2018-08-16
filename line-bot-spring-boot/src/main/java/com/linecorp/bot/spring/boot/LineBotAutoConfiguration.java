/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.spring.boot;

import java.net.URISyntaxException;

import javax.cache.Caching;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.linecorp.bot.spring.boot.support.ChannelTokenCache;
import com.linecorp.bot.spring.boot.support.ChannelTokenPropertiesResolverImpl;
import com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport;
import com.linecorp.bot.spring.boot.support.LineMessagingClientFactory;

/**
 * Also refers {@link LineBotWebMvcBeans} for web only beans definition.
 */
@Configuration
@EnableCaching
@AutoConfigureAfter(LineBotWebMvcConfigurer.class)
@EnableConfigurationProperties({LineBotProperties.class, ChannelTokenPropertiesResolverImpl.class})
@Import(LineMessageHandlerSupport.class)
public class LineBotAutoConfiguration {
	@Bean
	public LineMessagingClientFactory lineMessagingClientFactory() {
		return new LineMessagingClientFactory();
	}
	
	@Bean
	public ChannelTokenCache channelTokenCache(CacheManager manager) {
		return new ChannelTokenCache(manager);
	}
	
	@Bean
	@Conditional(MissingJCacheConfigCondition.class)
	public javax.cache.CacheManager cacheManager() throws URISyntaxException {
		return Caching.getCachingProvider()
			.getCacheManager(getClass().getResource("ehcache.xml").toURI(), getClass().getClassLoader());
	}
	
	static class MissingJCacheConfigCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return !context.getEnvironment().containsProperty("spring.cache.jcache.config");
		}		
	}
}
