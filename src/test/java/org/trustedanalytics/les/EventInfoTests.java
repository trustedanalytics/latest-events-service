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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.trustedanalytics.les.storage.EventInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class EventInfoTests {
    @Test
    public void equals_returnsFalseForNull() {
        // Arrange
        EventInfo info = new EventInfo();

        // Act / Assert
        assertFalse(info.equals(null));
    }

    @Test
    public void equals_returnsFalseForDifferentClasses() {
        // Arrange
        EventInfo info = new EventInfo();

        // Act / Assert
        assertFalse(info.equals("Test"));
        assertFalse(info.equals(new Object()));
    }

    @Test
    public void equals_returnsTrueForSameObject() {
        // Arrange
        EventInfo info = new EventInfo();

        // Act / Assert
        assertTrue(info.equals(info));
    }

    @Test
    public void equals_returnsTrueForEqualObjects() {
        // Arrange
        EventInfo a = new EventInfo();
        EventInfo b = new EventInfo();

        // Act / Assert
        assertTrue(a.equals(b));

        Date d = new Date();
        setValues(a, "10", "s11", "sName", "o12", 13L, "category", "msg", d);
        setValues(b, "10", "s11", "sName", "o12", 13L, "category", "msg", d);
        assertTrue(a.equals(b));

        // Update time should not be part of equals
        b.setUpdateTime(new Date(d.getTime() + 1000));
        assertTrue(a.equals(b));
    }

    @Test
    public void equals_returnsFalseForNotEqualObjects() {
        // Arrange
        EventInfo a = new EventInfo();
        EventInfo b = new EventInfo();
        Date d = new Date();
        setValues(a, "10", "s11", "sName", "o12", 13L, "category", "msg", d);

        // Act / Assert

        // Confirm it returns true first (same values)
        setValues(b, "10", "s11", "sName", "o12", 13L, "category", "msg", d);
        assertTrue(a.equals(b));

        // Now return false for a single different value
        setValues(b, "11", "s11", "sName", "o12", 13L, "category", "msg", d);
        assertFalse(a.equals(b));
        setValues(b, "10", "s12", "sName", "o12", 13L, "category", "msg", d);
        assertFalse(a.equals(b));
        setValues(b, "10", "s11", "sNAME", "o12", 13L, "category", "msg", d);
        assertFalse(a.equals(b));
        setValues(b, "10", "s11", "sName", "o13", 13L, "category", "msg", d);
        assertFalse(a.equals(b));
        setValues(b, "10", "s11", "sName", "o12", 14L, "category", "msg", d);
        assertFalse(a.equals(b));
        setValues(b, "10", "s11", "sName", "o12", 13L, "CATegory", "msg", d);
        assertFalse(a.equals(b));
        setValues(b, "10", "s11", "sName", "o12", 13L, "category", "MSG", d);
        assertFalse(a.equals(b));
    }

    private void setValues(
            EventInfo obj,
            String id,
            String sourceId,
            String sourceName,
            String organizationId,
            long timestamp,
            String category,
            String message,
            Date updateTime) {
        obj.setId(id);
        obj.setSourceId(sourceId);
        obj.setSourceName(sourceName);
        obj.setOrganizationId(organizationId);
        obj.setTimestamp(timestamp);
        obj.setCategory(category);
        obj.setMessage(message);
        obj.setUpdateTime(updateTime);
    }
}
