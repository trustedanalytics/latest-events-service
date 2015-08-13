/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.les.config;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import nats.client.MessageHandler;
import nats.client.Nats;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.trustedanalytics.les.MemoryEventStore;
import org.trustedanalytics.les.nats.EventRetriever;
import org.trustedanalytics.les.nats.NatsEventRetriever;
import org.trustedanalytics.les.storage.EventStore;

@Configuration
@Profile("integration-test")
public class TestConfig {
    private static MessageHandler messageHandler;

    public static MessageHandler messageHandler() {
        return messageHandler;
    }

    @Bean
    public EventStore eventStore() {
        return new MemoryEventStore();
    }

    @Bean
    public Nats nats() {
        return mock(Nats.class);
    }

    @Bean
    public EventRetriever eventRetriever(Nats nats) {
        EventRetriever retriever = new NatsEventRetriever(nats);
        ArgumentCaptor<MessageHandler> msgHandler = ArgumentCaptor.forClass(MessageHandler.class);
        verify(nats, times(1)).subscribe(eq("platform.>"), msgHandler.capture());
        messageHandler = msgHandler.getValue();
        return retriever;
    }
}
