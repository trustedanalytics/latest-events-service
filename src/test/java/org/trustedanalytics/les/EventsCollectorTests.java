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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.trustedanalytics.les.gathering.EventCollector;
import org.trustedanalytics.les.nats.EventProcessor;
import org.trustedanalytics.les.nats.EventRetriever;
import org.trustedanalytics.les.nats.NatsEventInfo;
import org.trustedanalytics.les.storage.EventInfo;
import org.trustedanalytics.les.storage.EventStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class EventsCollectorTests {

    private EventCollector sut;

    @Mock
    private EventRetriever retriever;

    @Mock
    private EventStore store;

    @Test
    public void constructor_collectorRegistersItselfAsEventProcessor() {
        sut = new EventCollector(retriever, store);

        verify(retriever, times(1)).setEventProcessor(any(EventProcessor.class));
    }

    @Test
    public void process_whenEventArrivesItIsStored() {
        sut = new EventCollector(retriever, store);
        ArgumentCaptor<EventProcessor> processor = ArgumentCaptor.forClass(EventProcessor.class);
        verify(retriever, times(1)).setEventProcessor(processor.capture());

        NatsEventInfo natsEvent = new NatsEventInfo();
        natsEvent.setServiceId("123-abc");
        natsEvent.setOrganizationId(UUID.randomUUID().toString());

        processor.getValue().process("service-creation", natsEvent);

        ArgumentCaptor<EventInfo> event = ArgumentCaptor.forClass(EventInfo.class);
        verify(store, times(1)).save(event.capture());
        assertNotNull(event.getValue());
        assertEquals("123-abc", event.getValue().getSourceId());
    }
}
