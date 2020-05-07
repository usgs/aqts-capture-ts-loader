package gov.usgs.wma.waterdata;

import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import static org.junit.jupiter.api.Assertions.*;

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
@ActiveProfiles("it")
public class LoadTimeSeriesIT extends BaseTestDao {

	@Autowired
	public LoadTimeSeries loadTimeSeries;

	@Test
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testInsert() {

		ResultObject result = loadTimeSeries.processRequest(request);
		Integer expectedCount = 3;
		assertEquals(expectedCount, result.getCount());
		assertEquals(LoadTimeSeries.STATUS_SUCCESS, result.getStatus());
		assertNull(result.getFailMessage());
	}

	@Test
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/")
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testReplace() {

		ResultObject result = loadTimeSeries.processRequest(request);
		Integer expectedCount = 3;
		assertEquals(expectedCount, result.getCount());
		assertEquals(LoadTimeSeries.STATUS_SUCCESS, result.getStatus());
		assertNull(result.getFailMessage());
	}

	@Test
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testNoRecordsFound() {
		request.setUniqueId(BAD_TS_UNIQUE_ID);
		ResultObject result = loadTimeSeries.processRequest(request);
		assertNull(result.getCount());
		assertEquals(LoadTimeSeries.STATUS_SUCCESS, result.getStatus());
		assertDoesNotThrow(() -> {
			loadTimeSeries.apply(request);
		}, "should have thrown an exception but did not");
	}

	@Test
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testNullUniqueId() {
		request.setUniqueId(null);
		ResultObject result = loadTimeSeries.processRequest(request);
		assertNull(result.getCount());
		assertEquals(LoadTimeSeries.STATUS_FAIL, result.getStatus());
		assertEquals(LoadTimeSeries.FAIL_MESSAGE_NULL_UNIQUE_ID, result.getFailMessage());
		assertThrows(RuntimeException.class, () -> {
			loadTimeSeries.apply(request);
		}, "should have thrown an exception but did not");
	}
}
