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

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class EventInfo {
    @Id
    private String id;

    private String sourceId;
    private String sourceName;
    private long timestamp;
    private String category;
    private String message;

    public EventInfo() {
    }

    public EventInfo(
            String id,
            String sourceId,
            String sourceName,
            long timestamp,
            String category,
            String message) {
        this.id = id;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.timestamp = timestamp;
        this.category = category;
        this.message = message;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getSourceId() { return sourceId; }

    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public String getSourceName() { return sourceName; }

    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventInfo that = (EventInfo) o;

        return Objects.equals(that.id, this.id)
                && Objects.equals(that.sourceId, this.sourceId)
                && Objects.equals(that.sourceName, this.sourceName)
                && Objects.equals(that.timestamp, this.timestamp)
                && Objects.equals(that.category, this.category)
                && Objects.equals(that.message, this.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceId, sourceName, timestamp, category, message);
    }
}
