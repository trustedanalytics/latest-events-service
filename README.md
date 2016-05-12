[![Build Status](https://travis-ci.org/trustedanalytics/latest-events-service.svg?branch=master)](https://travis-ci.org/trustedanalytics/latest-events-service)
[![Dependency Status](https://www.versioneye.com/user/projects/57236b5cba37ce00464e0544/badge.svg?style=flat)](https://www.versioneye.com/user/projects/57236b5cba37ce00464e0544)

# latest-events-service
Service gathering events from other platform components

### Application endpoints

### Local development
#### Running
##### Tests
###### Unit
```
mvn test
```

###### Integration
```
mvn integration-test
```

##### Application

###### Prerequisites

In order to run the service locally, two additional services are required to be up and running: MongoDB and NATS.

MongoDB can be downloaded from this web site: https://www.mongodb.org/. No special setup is required.

NATS can be downloaded from this web site: https://nats.io/. NATS can be started using this command:
```
./gnatsd -V
```

The `-V` option prints some extra output information to the console (useful for debugging).

###### Environment variables

To run the service locally or in Cloud Foundry, the following environment variables need to be defined:
* `VCAP_SERVICES_SSO_CREDENTIALS_TOKENKEY` - an UAA endpoint for verifying token signatures;
* `VCAP_SERVICES_SSO_CREDENTIALS_APIENDPOINT` - a Cloud Foundry API endpoint;
* `VCAP_SERVICES_LATEST_EVENTS_STORE_CREDENTIALS_HOSTNAME` - a MongoDB server name;
* `VCAP_SERVICES_LATEST_EVENTS_STORE_CREDENTIALS_PORT` - a MongoDB server port;
* `VCAP_SERVICES_LATEST_EVENTS_STORE_CREDENTIALS_DBNAME` - a MongoDB database name;
* `VCAP_SERVICES_LATEST_EVENTS_STORE_CREDENTIALS_USERNAME` - user name used to connect to MongoDB (can be empty);
* `VCAP_SERVICES_LATEST_EVENTS_STORE_CREDENTIALS_PASSWORD` - password used to connect to MongoDB;
* `VCAP_SERVICES_NATS_PROVIDER_CREDENTIALS_URL` - a URL for the NATS service;

###### Starting service

To run the application, please type:
```
mvn spring-boot:run
```

To change the default listening port (8080), please add an additional option ```-Dserver.port=9993```
