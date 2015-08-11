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

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoRepositories
@Profile({"cloud", "default"})
public class CloudMongoConfig extends AbstractMongoConfiguration {
    @Autowired
    private MongoProperties mongoProps;

    @Override
    protected String getDatabaseName() {
        return mongoProps.getDbName();
    }

    @Override
    public Mongo mongo() throws Exception {
        ServerAddress serverAddress = new ServerAddress(mongoProps.getServerName(), mongoProps.getServerPort());
        List<MongoCredential> credendialList = new ArrayList<>();

        String user = mongoProps.getUser();
        if (user != null && !user.isEmpty()) {
            credendialList.add(MongoCredential.createMongoCRCredential(user, mongoProps.getDbName(), mongoProps.getPassword().toCharArray()));
        }

        return new MongoClient(serverAddress, credendialList);
    }
}
