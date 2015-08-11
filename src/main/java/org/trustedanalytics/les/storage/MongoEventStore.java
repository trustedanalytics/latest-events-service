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
package org.trustedanalytics.les.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;

public class MongoEventStore implements EventStore {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private MongoOperations mongoOperations;

    @Autowired
    public MongoEventStore(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void save(EventInfo eventInfo) {
        mongoOperations.insert(eventInfo);
    }

    @Override
    public List<EventInfo> getLatestEvents(Collection<String> orgs, int from, int size) {
        LOG.debug("getLatestEvents({}, {}, {})", orgs, from, size);

        Query query = new Query()
                .addCriteria(Criteria.where("organizationId").in(orgs))
                .skip(from)
                .limit(size)
                .with(new Sort(Sort.Direction.DESC, "timestamp"));
        return mongoOperations.find(query, EventInfo.class);
    }

    @Override
    public long getEventsCount(Collection<String> orgs) {
        LOG.debug("getEventsCount({})", orgs);

        Query query = new Query().addCriteria(Criteria.where("organizationId").in(orgs));
        return mongoOperations.count(query, EventInfo.class);
    }
}
