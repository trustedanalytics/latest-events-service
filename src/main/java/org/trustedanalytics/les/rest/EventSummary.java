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
package org.trustedanalytics.les.rest;

import org.trustedanalytics.les.storage.EventInfo;

import java.util.List;

public class EventSummary {

    private long total;

    private List<EventInfo> events;

    public EventSummary() {
    }

    public EventSummary(long total, List<EventInfo> events) {
        this.total = total;
        this.events = events;
    }

    public long getTotal() { return total; }

    public void setTotal(long total) { this.total = total; }

    public List<EventInfo> getEvents() { return events; }

    public void setEvents(List<EventInfo> events) { this.events = events; }
}
