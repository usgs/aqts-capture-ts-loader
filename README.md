# Aquarius Timeseries (AQTS) Load Timeseries Data

[![Build Status](https://travis-ci.com/usgs/aqts-capture-ts-loader.svg?branch=master)](https://travis-ci.com/usgs/aqts-capture-ts-loader)
[![codecov](https://codecov.io/gh/usgs/aqts-capture-ts-loader/branch/master/graph/badge.svg)](https://codecov.io/gh/usgs/aqts-capture-ts-loader)

Loads daily value time series from the transform database into the observation database for use by the observations service.

## Testing
This project contains JUnit tests. Maven can be used to run them (in addition to the capabilities of your IDE).

### Docker Network
A named Docker Network is needed to run the automated tests via maven. The following is a sample command for creating your own local network. In this example the name is aqts and the ip addresses will be 172.25.0.x

```.sh
docker network create --subnet=172.25.0.0/16 aqts
```

### Unit Testing
To run the unit tests of the application use:

```.sh
mvn package
```

### Database Integration Testing with Maven
To additionally start up both the transform and observation Docker databases and run the integration tests of the application use:

```.sh
mvn verify \
    -DTRANSFORM_TESTING_DATABASE_PORT=5437 \
    -DLOCAL_TRANSFORM_TESTING_DATABASE_PORT=5437 \
    -DTRANSFORM_TESTING_DATABASE_ADDRESS=localhost \
    -DTESTING_DATABASE_NETWORK=aqts \
    -DOBSERVATION_TESTING_DATABASE_PORT=5444 \
    -DLOCAL_OBSERVATION_TESTING_DATABASE_PORT=5444 \
    -DOBSERVATION_TESTING_DATABASE_ADDRESS=127.0.0.1
```

### Database Integration Testing with an IDE
To run tests against local transform and observation Docker databases use:

Transform database:
```.sh
docker run -p 127.0.0.1:5437:5432/tcp usgswma/aqts_capture_db:ci
```

Observation database: 
```.sh
docker run -p 127.0.0.1:5444:5432/tcp usgswma/wqp_db:etl
```

Additionally, add an application.yml configuration file at the project root (the following is an example):
```.yaml
TRANSFORM_DATABASE_ADDRESS: localhost
TRANSFORM_DATABASE_PORT: 5437
TRANSFORM_DATABASE_NAME: database_name
TRANSFORM_SCHEMA_NAME: schema_name
TRANSFORM_SCHEMA_OWNER_USERNAME: schema_owner
TRANSFORM_SCHEMA_OWNER_PASSWORD: changeMe

OBSERVATION_DATABASE_ADDRESS: 127.0.0.1
OBSERVATION_DATABASE_PORT: 5444
OBSERVATION_DATABASE_NAME: wqp_db
OBSERVATION_SCHEMA_NAME: nwis
OBSERVATION_SCHEMA_OWNER_USERNAME: nwis_ws_star
OBSERVATION_SCHEMA_OWNER_PASSWORD: changeMe
```