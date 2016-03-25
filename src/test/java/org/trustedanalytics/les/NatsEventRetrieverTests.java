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
package org.trustedanalytics.les;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.trustedanalytics.les.nats.EventProcessor;
import org.trustedanalytics.les.nats.NatsEventInfo;
import org.trustedanalytics.les.nats.NatsEventRetriever;

import nats.client.Message;
import nats.client.MessageHandler;
import nats.client.Nats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NatsEventRetrieverTests {

    private NatsEventRetriever sut;

    @Mock
    private Nats nats;

    @Test
    public void constructor_subscribesToNats() {
        sut = new NatsEventRetriever(nats);

        verify(nats, atLeastOnce()).subscribe(eq("platform.>"), any(MessageHandler.class));
    }

    @Test
    public void setEventProcessor_messagesAreFlowingEvenIfProcessorIsNull() {
        // Arrange
        Message message = mock(Message.class);
        when(message.getSubject()).thenReturn("platform.subject");
        when(message.getBody())
                .thenReturn("{\"ServiceId\":\"640A1E39-D5A4-408D-85E5-72A44A383425\",\"ServiceType\":\"\",\"Message\":\"Service spawning failed with error: exit status 1\",\"Timestamp\":\"1438169648514\"}");
        sut = new NatsEventRetriever(nats);
        sut.setEventProcessor(null);

        // Verify that subscription happened and capture the message handler
        ArgumentCaptor<MessageHandler> msgHandler = ArgumentCaptor.forClass(MessageHandler.class);
        verify(nats, times(1)).subscribe(eq("platform.>"), msgHandler.capture());

        // On captured message handler, simulate message arrival. No error should be thrown.
        msgHandler.getValue().onMessage(message);
    }

    @Test
    public void setEventProcessor_assignedProcessorHandlesMessages() {
        // Arrange
        EventProcessor processor = mock(EventProcessor.class);
        Message message = mock(Message.class);
        when(message.getSubject()).thenReturn("platform.subject");
        when(message.getBody())
                .thenReturn("{\"ServiceId\":\"640A1E39-D5A4-408D-85E5-72A44A383425\",\"ServiceType\":\"\",\"Message\":\"Service spawning failed with error: exit status 1\",\"Timestamp\":\"1438169648514\"}");
        sut = new NatsEventRetriever(nats);
        sut.setEventProcessor(processor);

        // Verify that subscription happened and capture the message handler
        ArgumentCaptor<MessageHandler> msgHandler = ArgumentCaptor.forClass(MessageHandler.class);
        verify(nats, times(1)).subscribe(eq("platform.>"), msgHandler.capture());

        // On captured message handler, simulate message arrival
        msgHandler.getValue().onMessage(message);

        // Verify that message was de-serialized and processed
        verify(processor, times(1)).process(eq("subject"), any(NatsEventInfo.class));
    }

    @Test
    public void setEventProcessor_exceptionInProcessorDoesNotBreakMessageFlow() {
        // Arrange
        EventProcessor processor = mock(EventProcessor.class);
        doThrow(new RuntimeException("Some error")).when(processor).process(any(String.class), any(NatsEventInfo.class));

        Message message = mock(Message.class);
        when(message.getSubject()).thenReturn("platform.subject");
        when(message.getBody())
                .thenReturn("{\"ServiceId\":\"640A1E39-D5A4-408D-85E5-72A44A383425\",\"ServiceType\":\"\",\"Message\":\"Service spawning failed with error: exit status 1\",\"Timestamp\":\"1438169648514\"}");
        sut = new NatsEventRetriever(nats);
        sut.setEventProcessor(processor);

        // Verify that subscription happened and capture the message handler
        ArgumentCaptor<MessageHandler> msgHandler = ArgumentCaptor.forClass(MessageHandler.class);
        verify(nats, times(1)).subscribe(eq("platform.>"), msgHandler.capture());

        // On captured message handler, simulate message arrival
        msgHandler.getValue().onMessage(message);
        // Make sure this works twice
        msgHandler.getValue().onMessage(message);

        // Verify that message was de-serialized and processed
        verify(processor, times(2)).process(eq("subject"), any(NatsEventInfo.class));
    }
}
