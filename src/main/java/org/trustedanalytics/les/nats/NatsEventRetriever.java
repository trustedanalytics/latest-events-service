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
package org.trustedanalytics.les.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import nats.client.Message;
import nats.client.Nats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class NatsEventRetriever implements EventRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Prefix that will be removed from the message subject.
     */
    private static final String NATS_SUBJECT_PREFIX = "platform.";

    /**
     * Subject to subscribe to (matches any length of the tail of the subject).
     */
    private static final String NATS_SUBJECT = NATS_SUBJECT_PREFIX + ">";

    private EventProcessor processor;

    private ObjectMapper mapper;

    public NatsEventRetriever(Nats nats) {
        this.mapper = new ObjectMapper();

        nats.subscribe(NATS_SUBJECT, this::onMessageArrived);
    }

    @Override
    public void setEventProcessor(EventProcessor processor) {
        if (processor != null) {
            this.processor = processor;
        }
    }

    private void onMessageArrived(Message msg) {
        String body = msg.getBody();
        LOG.debug("Event message: " + body);

        try {
            NatsEventInfo eventInfo = mapper.readValue(body, NatsEventInfo.class);
            if (processor != null) {
                processor.process(readCategoryFromSubject(msg.getSubject()), eventInfo);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private String readCategoryFromSubject(String subject) {
        String category = subject;
        if (category != null && category.startsWith(NATS_SUBJECT_PREFIX)) {
            category = category.substring(NATS_SUBJECT_PREFIX.length());
        }

        return category;
    }
}
