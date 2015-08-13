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
package org.trustedanalytics.les.gathering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trustedanalytics.les.nats.EventProcessor;
import org.trustedanalytics.les.nats.EventRetriever;
import org.trustedanalytics.les.nats.NatsEventInfo;
import org.trustedanalytics.les.storage.EventInfo;
import org.trustedanalytics.les.storage.EventStore;

import java.lang.invoke.MethodHandles;

@Component
public class EventCollector {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public EventCollector(EventRetriever eventRetriever, EventStore eventStore) {
        // The only thing the collector is doing is "marrying" two independent entities: EventRetriever and EventStore
        eventRetriever.setEventProcessor(new EventProcessorImpl(eventStore));
    }

    private static class EventProcessorImpl implements EventProcessor {
        private static final String NATS_EVENT_SOURCE_NAME = "NATS";

        private EventStore eventStore;

        public EventProcessorImpl(EventStore eventStore) {
            this.eventStore = eventStore;
        }

        @Override
        public void process(String category, NatsEventInfo event) {
            EventInfo eventInfo = new EventInfo(
                    null,
                    event.getServiceId(),
                    NATS_EVENT_SOURCE_NAME,
                    event.getTimestamp(),
                    category,
                    event.getMessage());

            LOG.debug("Processing event: {}: {}", event.getServiceId(), event.getTimestamp());

            eventStore.save(eventInfo);

            LOG.debug("Event processed: {}: {}", event.getServiceId(), event.getTimestamp());
        }
    }
}
