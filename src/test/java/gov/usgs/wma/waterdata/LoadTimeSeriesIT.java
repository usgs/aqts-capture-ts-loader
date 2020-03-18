package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(
		connection="transform",
		value="classpath:/testData/transformDb/groundwaterStatisticalDailyValue/")
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
	}

	@Test
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testNotFound() {
		request.setUniqueId("badTimeSeriesUniqueId");
		ResultObject actualInsert = loadTimeSeries.processRequest(request);
		Integer expectedCount = 0;
		assertEquals(expectedCount, actualInsert.getCount());
		assertEquals(LoadTimeSeries.STATUS_FAIL, actualInsert.getStatus());
	}
}
