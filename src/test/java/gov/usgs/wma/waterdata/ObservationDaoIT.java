package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

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
			ObservationDao.class})

@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(
		dataSetLoader=FileSensingDataSetLoader.class,
		databaseConnection={"observation"}
)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public class ObservationDaoIT {

	@Autowired
	public ObservationDao observationDao;

	public RequestObject request;
	public static final String tsUniqueId = "17f83e62b06e4dc29e78d96b4426a255";
	public TimeSeries timeSeries1 = new TimeSeries();
	public TimeSeries timeSeries2 = new TimeSeries();
	public TimeSeries timeSeries3 = new TimeSeries();

	@BeforeEach
	public void setup() {
		request = new RequestObject();
		request.setUniqueId(tsUniqueId);

		String approvals = "[\"Approved\"]";
		String grades = "[\"50\"]";
		String qualifiers = null;

		timeSeries1.setGroundwaterDailyValueIdentifier("USGS-132624144452771-17f83e62b06e4dc29e78d96b4426a255");
		timeSeries1.setTimeSeriesUniqueId("17f83e62b06e4dc29e78d96b4426a255");
		timeSeries1.setMonitoringLocationIdentifier("USGS-132624144452771");
		timeSeries1.setObservedPropertyId("62610");
		timeSeries1.setStatisticId("00001");
		timeSeries1.setTimeStep(Date.valueOf("2008-06-03"));
		timeSeries1.setUnitOfMeasure("ft");
		timeSeries1.setResult("36.02");
		timeSeries1.setApprovals(approvals);
		timeSeries1.setQualifiers(qualifiers);
		timeSeries1.setGrades(grades);

		timeSeries2.setGroundwaterDailyValueIdentifier("USGS-132624144452771-17f83e62b06e4dc29e78d96b4426a255");
		timeSeries2.setTimeSeriesUniqueId("17f83e62b06e4dc29e78d96b4426a255");
		timeSeries2.setMonitoringLocationIdentifier("USGS-132624144452771");
		timeSeries2.setObservedPropertyId("62610");
		timeSeries2.setStatisticId("00001");
		timeSeries2.setTimeStep(Date.valueOf("2008-06-04"));
		timeSeries2.setUnitOfMeasure("ft");
		timeSeries2.setResult("35.96");
		timeSeries2.setApprovals(approvals);
		timeSeries2.setQualifiers(qualifiers);
		timeSeries2.setGrades(grades);

		timeSeries3.setGroundwaterDailyValueIdentifier("USGS-132624144452771-17f83e62b06e4dc29e78d96b4426a255");
		timeSeries3.setTimeSeriesUniqueId("17f83e62b06e4dc29e78d96b4426a255");
		timeSeries3.setMonitoringLocationIdentifier("USGS-132624144452771");
		timeSeries3.setObservedPropertyId("62610");
		timeSeries3.setStatisticId("00001");
		timeSeries3.setTimeStep(Date.valueOf("2008-06-05"));
		timeSeries3.setUnitOfMeasure("ft");
		timeSeries3.setResult("35.91");
		timeSeries3.setApprovals(approvals);
		timeSeries3.setQualifiers(qualifiers);
		timeSeries3.setGrades(grades);
	}

	@Test
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/")
	@ExpectedDatabase(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterDelete/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void testDelete() {

		// delete existing data
		Integer actualRowsDeletedCount = observationDao.deleteTimeSeries(request.getUniqueId());
		assertNotNull(actualRowsDeletedCount);
		Integer expectedRowsDeletedCount = 3;
		assertEquals(expectedRowsDeletedCount, actualRowsDeletedCount);
	}

	@Test
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void testInsert() {

		// insert new data
		Integer actualRowsInsertedCount = observationDao.insertTimeSeries(timeSeries1);
		assertNotNull(actualRowsInsertedCount);
		Integer expectedRowsInsertedCount = 1;
		assertEquals(expectedRowsInsertedCount, actualRowsInsertedCount);

		observationDao.insertTimeSeries(timeSeries2);
		observationDao.insertTimeSeries(timeSeries3);
	}
}