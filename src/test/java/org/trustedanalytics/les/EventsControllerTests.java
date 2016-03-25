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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.trustedanalytics.cloud.cc.api.CcOperations;
import org.trustedanalytics.les.rest.EventSummary;
import org.trustedanalytics.les.rest.EventsController;
import org.trustedanalytics.les.storage.EventStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class EventsControllerTests {

    private EventsController sut;

    @Mock
    private EventStore store;

    @Mock
    private CcOperations ccOperations;

    @Test
    public void getLatestEvents_retrievesDataFromStore() {
        when(store.getEventsCount(any(Collection.class))).thenReturn(1L);
        when(store.getLatestEvents(any(Collection.class), eq(2), eq(4))).thenReturn(new ArrayList<>());
        when(ccOperations.getOrgs()).thenReturn(Observable.empty());
        sut = new EventsController(store, ccOperations);

        EventSummary eventSummary = sut.getLatestEvents(null, 2, 4);

        verify(store, times(1)).getEventsCount(any(Collection.class));
        verify(store, times(1)).getLatestEvents(any(Collection.class), eq(2), eq(4));
        assertNotNull(eventSummary);
        assertEquals(1L, eventSummary.getTotal());
        assertNotNull(eventSummary.getEvents());
    }
}
