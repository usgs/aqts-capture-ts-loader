package gov.usgs.wma.waterdata;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(
		dataSetLoader=FileSensingDataSetLoader.class,
		databaseConnection={
			"transform",
			"observation"})
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public abstract class BaseTestDao {

	public static final String TS_UNIQUE_ID = "17f83e62b06e4dc29e78d96b4426a255";
	public static final String BAD_TS_UNIQUE_ID = "badTimeSeriesUniqueId";
	public RequestObject request;
	public TimeSeries timeSeries1;
	public TimeSeries timeSeries2;
	public TimeSeries timeSeries3;

	@BeforeEach
	public void setup() {
		request = new RequestObject();
		request.setUniqueId(TS_UNIQUE_ID);

		String approvals = "[\"Approved\"]";
		String grades = "[\"50\"]";
		String qualifiers = null;

		timeSeries1 = new TimeSeries();
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

		timeSeries2 = new TimeSeries();
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

		timeSeries3 =  new TimeSeries();
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
}
