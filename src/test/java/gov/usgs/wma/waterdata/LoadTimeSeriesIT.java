package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;


import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
		classes={
			DBTestConfig.class,
			LoadTimeSeries.class,
			TransformDao.class,
			ObservationDao.class})
@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(
		dataSetLoader=FileSensingDataSetLoader.class,
		databaseConnection={"transform", "observation"}
)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public class LoadTimeSeriesIT {

	@Autowired
	public LoadTimeSeries loadTimeSeries;

	public RequestObject request;
	public static final String tsUniqueId = "17f83e62b06e4dc29e78d96b4426a255";

	@BeforeEach
	public void setup() {
		request = new RequestObject();
		request.setUniqueId(tsUniqueId);
	}

	@Test
	@DatabaseSetup(
			connection="transform",
			value="classpath:/testData/transformDb/groundwaterStatisticalDailyValue/")
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testLoadTimeSeries() {

		ResultObject actualInsert = loadTimeSeries.processRequest(request);
		Integer expectedCount = 3;
		String expectedStatus = "success";
		assertEquals(expectedCount, actualInsert.getCount());
		assertEquals(expectedStatus, actualInsert.getStatus());
	}

	@Test
	@DatabaseSetup(
			connection="transform",
			value="classpath:/testData/transformDb/groundwaterStatisticalDailyValue/")
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED,
			connection="observation")
	public void testNotFound() {
		request.setUniqueId("badTimeSeriesUniqueId");
		ResultObject actualInsert = loadTimeSeries.processRequest(request);
		Integer expectedCount = 0;
		String expectedStatus = "success";
		assertEquals(expectedCount, actualInsert.getCount());
		assertEquals(expectedStatus, actualInsert.getStatus());
	}
}
