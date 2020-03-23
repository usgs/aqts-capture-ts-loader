package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE,
		classes={
			DBTestConfig.class,
			TransformDao.class})
@DatabaseSetup("classpath:/testData/transformDb/")
@ActiveProfiles("it")
public class TransformDaoIT extends BaseTestDao {

	@Autowired
	public TransformDao transformDao;
	List<TimeSeries> timeSeriesList;

	@BeforeEach
	public void setupTransformDaoIT() {
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

	@Test
	public void testNotFound() {
		// try to get data using a bad unique id
		List<TimeSeries> expectedTimeSeriesList = new ArrayList();
		request.setUniqueId(BAD_TS_UNIQUE_ID);
		List<TimeSeries> actualData = transformDao.getTimeSeries(request.getUniqueId());
		assertEquals(expectedTimeSeriesList, actualData);
	}
}