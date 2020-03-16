package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

	@BeforeEach
	public void setup() {
		request = new RequestObject();
		request.setUniqueId(tsUniqueId);
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
}