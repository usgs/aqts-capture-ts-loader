package gov.usgs.wma.waterdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
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
import org.springframework.util.LinkedCaseInsensitiveMap;


@SpringBootTest(webEnvironment=WebEnvironment.NONE,
		classes={DBTestConfig.class, TransformDao.class})
@DatabaseSetup("classpath:/testData/transformDb/groundwaterStatisticalDailyValue/")

@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(
		dataSetLoader=FileSensingDataSetLoader.class,
		databaseConnection={"transform"}
)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public class TransformDaoIT {

	@Autowired
	public TransformDao transformDao;

	public RequestObject request;
	List<TimeSeries> timeSeriesList;
	public static final String tsUniqueId = "17f83e62b06e4dc29e78d96b4426a255";

	@BeforeEach
	public void setup() {
		request = new RequestObject();
		request.setUniqueId(tsUniqueId);
//		Object approvals;
//		{
			String approvals = "[\"Approved\"]";
//			approvals.setType("jsonb");
//			approvals.setValue("[\"Approved\"]");
//		}
//		Object grades;
//		{
			String grades = "[\"50\"]";
//			grades.setType("jsonb");
//			grades.setValue("[\"50\"]");
//		}
		
//		Object qualifiers;
//		{
			String qualifiers = null;
//			qualifiers.setType("jsonb");
//			qualifiers.setValue("NULL");
//		}

		TimeSeries timeSeries1 = new TimeSeries();
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

		TimeSeries timeSeries2 = new TimeSeries();
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

		TimeSeries timeSeries3 = new TimeSeries();
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

		timeSeriesList = new ArrayList<>();

		timeSeriesList.add(timeSeries1);
		timeSeriesList.add(timeSeries2);
		timeSeriesList.add(timeSeries3);
	}

	@Test
	public void testGet() {

		// get new data, return unique ids
		List<TimeSeries> actualData = transformDao.getTimeSeries(request.getUniqueId());
		assertNotNull(actualData);
		assertEquals(timeSeriesList, actualData);
	}


}