package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import java.util.List;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE,
		classes={
			DBTestConfig.class,
			ObservationDao.class})
@ActiveProfiles("it")
public class ObservationDaoIT extends BaseTestDao {

	@Autowired
	public ObservationDao observationDao;

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
			value="classpath:/testData/observationDb/")
	@DatabaseSetup(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/empty/")
	@ExpectedDatabase(
			connection="observation",
			value="classpath:/testResult/observationDb/groundwaterDailyValue/afterInsert/",
			assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void testInsert() {
		// insert new data
		Integer actualRowsInsertedCount = observationDao.insertTimeSeries(TS_UNIQUE_ID, List.of(timeSeries1, timeSeries2, timeSeries3));
		assertNotNull(actualRowsInsertedCount);
		Integer expectedRowsInsertedCount = 3;
		assertEquals(expectedRowsInsertedCount, actualRowsInsertedCount);
	}
}
