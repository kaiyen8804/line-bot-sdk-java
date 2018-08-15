/*
 * Copyright 2018 LINE Corporation
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

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor;
import com.linecorp.bot.spring.boot.support.LineBotServerArgumentProcessor;

@Component
@ConditionalOnWebApplication
public class LineBotWebMvcBeans {
    @Autowired
    private LineBotProperties lineBotProperties;

    /**
     * Expose {@link LineBotServerArgumentProcessor} as {@link Bean}.
     */
    @Bean
    public LineBotServerArgumentProcessor lineBotServerArgumentProcessor() {
        return new LineBotServerArgumentProcessor();
    }

    /**
     * Expose {@link LineBotServerInterceptor} as {@link Bean}.
     */
    @Bean
    public LineBotServerInterceptor lineBotServerInterceptor() {
        return new LineBotServerInterceptor();
    }

    /**
     * Expose {@link LineSignatureValidator} as {@link Bean}.
     */
    @Bean
    public LineSignatureValidator lineSignatureValidator() {
        return new LineSignatureValidator(
                lineBotProperties.getChannelSecrets().stream()
                	.map(channelSecret -> channelSecret.getBytes(StandardCharsets.US_ASCII))
                	.collect(Collectors.toList())
        );
    }

    /**
     * Expose {@link LineBotCallbackRequestParser} as {@link Bean}.
     */
    @Bean
    public LineBotCallbackRequestParser lineBotCallbackRequestParser(
            LineSignatureValidator lineSignatureValidator) {
        return new LineBotCallbackRequestParser(lineSignatureValidator);
    }
}
