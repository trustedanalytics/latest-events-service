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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.trustedanalytics.les.storage.EventInfo;
import org.trustedanalytics.les.storage.cloud.EventInfoRepository;
import org.trustedanalytics.les.storage.cloud.MongoEventStore;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MongoEventStoreTests {

    private MongoEventStore sut;

    @Mock
    private EventInfoRepository repository;

    @Mock
    private MongoOperations mongoOperations;

    @Test
    public void callingSaveShouldInsertRecordToRepository() {
        sut = new MongoEventStore(repository, mongoOperations);

        EventInfo e = new EventInfo();
        sut.save(e);

        verify(repository, times(1)).insert(e);
    }

    @Test
    public void callingGetEventsCountShouldCallCountOnRepository() {
        when(repository.count())
                .thenReturn(123L);

        sut = new MongoEventStore(repository, mongoOperations);
        long count = sut.getEventsCount();

        verify(repository, times(1)).count();
        assertEquals(123L, count);
    }

    @Test
    public void callingGetLatestEventsShouldCallFindOnMongoOperations() {
        List<EventInfo> events = new ArrayList<>();
        events.add(new EventInfo());
        when(mongoOperations.find(any(Query.class), eq(EventInfo.class)))
                .thenReturn(events);

        sut = new MongoEventStore(repository, mongoOperations);
        List<EventInfo> actualEvents = sut.getLatestEvents(1, 5);

        verify(mongoOperations, times(1)).find(any(Query.class), eq(EventInfo.class));
        assertEquals(events.size(), actualEvents.size());
        assertEquals(events.get(0), actualEvents.get(0));
    }
}
