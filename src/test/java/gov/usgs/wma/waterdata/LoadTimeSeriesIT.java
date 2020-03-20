package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE,
		classes={
			DBTestConfig.class,
			LoadTimeSeries.class,
			TransformDao.class,
			ObservationDao.class})
@DatabaseSetup(
		connection="transform",
		value="classpath:/testData/transformDb/")
@DatabaseSetup(
		connection="observation",
		value="classpath:/testData/observationDb/")
@DatabaseSetup(
		connection="observation",
		value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
@ActiveProfiles("it")
public class LoadTimeSeriesIT extends BaseTestDao {

	@Autowired
	public LoadTimeSeries loadTimeSeries;

	@Test
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testLoadTimeSeries() {

		ResultObject actualInsert = loadTimeSeries.processRequest(request);
		Integer expectedCount = 3;
		assertEquals(expectedCount, actualInsert.getCount());
		assertEquals(LoadTimeSeries.STATUS_SUCCESS, actualInsert.getStatus());
		assertEquals(null, actualInsert.getFailMessage());
	}

	@Test
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testNoRecordsFound() {
		request.setUniqueId("badTimeSeriesUniqueId");
		ResultObject actualInsert = loadTimeSeries.processRequest(request);
		Integer expectedCount = null;
		assertEquals(expectedCount, actualInsert.getCount());
		assertEquals(LoadTimeSeries.STATUS_FAIL, actualInsert.getStatus());
		assertEquals(LoadTimeSeries.FAIL_MESSAGE_NO_RECORDS, actualInsert.getFailMessage());
	}

	@Test
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testNullUniqueId() {
		request.setUniqueId(null);
		ResultObject actualInsert = loadTimeSeries.processRequest(request);
		Integer expectedCount = null;
		assertEquals(expectedCount, actualInsert.getCount());
		assertEquals(LoadTimeSeries.STATUS_FAIL, actualInsert.getStatus());
		assertEquals(LoadTimeSeries.FAIL_MESSAGE_NULL_UNIQUE_ID, actualInsert.getFailMessage());
	}
}
