package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;


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
			ObservationDao.class,
			TransformDao.class})
@DatabaseSetup("classpath:/testData/transformDb/groundwaterStatisticalDailyValue/")

@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(
		dataSetLoader=FileSensingDataSetLoader.class,
		databaseConnection={"observation", "transform"}
)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public class ObservationDaoIT {

	@Autowired
	private ObservationDao observationDao;

	@Autowired
	private TransformDao transformDao;
	private RequestObject timeSeriesUniqueId = new RequestObject();

	@Test
	public void testInsert() {

		// get new data, return unique ids
		timeSeriesUniqueId.setUniqueId("someTimeSeriesUniqueIdFromTestData");
		List<TimeSeries> actualData = transformDao.getTimeSeries(timeSeriesUniqueId.getUniqueId());
		assertNotNull(actualData);
		System.out.println(actualData);
	}

	@Test
	public void testDelete() {

		// get new data, return unique ids
		timeSeriesUniqueId.setUniqueId("someTimeSeriesUniqueIdFromTestData");
		List<TimeSeries> actualData = transformDao.getTimeSeries(timeSeriesUniqueId.getUniqueId());
		assertNotNull(actualData);
		System.out.println(actualData);
	}
}