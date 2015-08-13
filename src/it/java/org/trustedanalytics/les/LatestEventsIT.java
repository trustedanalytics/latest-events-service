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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nats.client.Message;
import nats.client.MessageHandler;
import nats.client.Nats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.trustedanalytics.les.config.TestConfig;
import org.trustedanalytics.les.rest.EventSummary;
import org.trustedanalytics.les.rest.EventsController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"integration-test"})
public class LatestEventsIT {

    @Value("http://localhost:${local.server.port}")
    private String BASE_URL;

    @Autowired
    private Nats nats;

    @Test
    public void callGetLatestEventsEndpointShouldReturnEventsCollectedFromNats() {
        // Arrange
        String url = BASE_URL + EventsController.LATEST_EVENTS_URL;
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        // Make sure initially there is no events collected
        ResponseEntity<EventSummary> response = testRestTemplate.getForEntity(url, EventSummary.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        EventSummary es = response.getBody();
        assertNotNull(es);
        assertEquals(0L, es.getTotal());
        assertEquals(0, es.getEvents().size());

        // Generate one event through nats
        MessageHandler msgHandler = TestConfig.messageHandler();
        Message message = mock(Message.class);
        when(message.getSubject()).thenReturn("platform.subject");
        when(message.getBody())
                .thenReturn("{\"ServiceId\":\"640A1E39-D5A4-408D-85E5-72A44A383425\",\"ServiceType\":\"\",\"Message\":\"Service spawning failed with error: exit status 1\",\"Timestamp\":\"1438169648514\"}");
        msgHandler.onMessage(message);

        // Call API again to confirm message was processed and is available through REST service
        response = testRestTemplate.getForEntity(url, EventSummary.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        es = response.getBody();
        assertNotNull(es);
        assertEquals(1L, es.getTotal());
        assertEquals(1, es.getEvents().size());
        assertEquals("640A1E39-D5A4-408D-85E5-72A44A383425", es.getEvents().get(0).getSourceId());
    }
}
