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

package com.example.bot.spring.echo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.Lists;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.LineBotProperties;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.support.ChannelTokenCache;
import com.linecorp.bot.spring.boot.support.ChannelTokenPropertiesResolverImpl;
import com.linecorp.bot.spring.boot.support.LineMessagingClientFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	
	protected List<String> groupIds = Lists.newArrayList();
    @Autowired
    private LineMessagingClientFactory lineMessagingClientFactory;
    @Autowired
    private ChannelTokenCache channelTokenCache;
	
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }
    
    @Bean
    CommandLineRunner debug(LineBotProperties lineBotProperties, 
    		ChannelTokenPropertiesResolverImpl channelTokenResolver,
    		ChannelTokenCache channelTokenCache,  
    		CacheManager manager) {
    	return args -> {
    		log.info("lineBotProperties = {}", lineBotProperties);
    		log.info("channelTokenResolver = {}", channelTokenResolver);
    		log.info("cache = {}", manager.getCache("channelToken"));
    	};
    }
    
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
    	lineMessagingClientFactory.get(channelTokenCache.channelToken(event.getReplyToken()))
    		.pushMessage(new PushMessage(event.getSource().getSenderId(), new TextMessage(event.getMessage().getText())));
//    	return new TextMessage(event.getMessage().getText());
    }
    
    @EventMapping
    public Message handleJoinEvent(JoinEvent event) {
	    if(event.getSource() instanceof GroupSource) {
        	String groupId = ((GroupSource) event.getSource()).getGroupId(); 
        	groupIds.add(groupId);
        	log.info("group Id: {}", groupId);
        	return new TextMessage("Welcome to iEN");
        }	            	
    	return null;
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
