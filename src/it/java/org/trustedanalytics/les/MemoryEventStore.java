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


import org.trustedanalytics.les.storage.EventInfo;
import org.trustedanalytics.les.storage.EventStore;

import java.util.ArrayList;
import java.util.List;

public class MemoryEventStore implements EventStore {
    private List<EventInfo> list = new ArrayList<>();

    @Override
    public void save(EventInfo eventInfo)
    {
        list.add(eventInfo);
    }

    @Override
    public List<EventInfo> getLatestEvents(int from, int size) {
        List<EventInfo> result = new ArrayList<>();
        int maxIndexExclusive = from + size;
        if (maxIndexExclusive > list.size()) {
            maxIndexExclusive = list.size();
        }

        for (int i = from; i < maxIndexExclusive; i++) {
            result.add(list.get(i));
        }

        return result;
    }

    @Override
    public long getEventsCount() {
        return list.size();
    }
}
