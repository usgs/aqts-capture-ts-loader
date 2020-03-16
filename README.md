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

### Database Integration Testing
To additionally start up both the transform and observation Docker databases and run the integration tests of the application use:

```.sh
mvn verify \
    -DAQTS_TESTING_DATABASE_PORT=5437 \
    -DAQTS_TESTING_DATABASE_ADDRESS=localhost \
    -DAQTS_TESTING_DATABASE_NETWORK=aqts \
    -DOBSERVATION_TESTING_DATABASE_PORT=5444 \
    -DOBSERVATION_TESTING_DATABASE_ADDRESS=localhost \
    -DOBSERVATION_TESTING_DATABASE_NETWORK=aqts
```