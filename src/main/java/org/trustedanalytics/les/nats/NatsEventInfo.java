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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NatsEventInfo {

    private String serviceId;

    private String serviceName;

    private String serviceType;

    private String organizationId;

    private String message;

    private long timestamp;

    @JsonProperty("ServiceId")
    public String getServiceId() { return serviceId; }

    @JsonProperty("ServiceId")
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    @JsonProperty("ServiceName")
    public String getServiceName() { return serviceName; }

    @JsonProperty("ServiceName")
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    @JsonProperty("ServiceType")
    public String getServiceType() { return serviceType; }

    @JsonProperty("ServiceType")
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    @JsonProperty("OrgGuid")
    public String getOrganizationId() { return organizationId; }

    @JsonProperty("OrgGuid")
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }

    @JsonProperty("Message")
    public String getMessage() { return message; }

    @JsonProperty("Message")
    public void setMessage(String message) { this.message = message; }

    @JsonProperty("Timestamp")
    public long getTimestamp() { return timestamp; }

    @JsonProperty("Timestamp")
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
