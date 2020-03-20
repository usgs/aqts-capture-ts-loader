package gov.usgs.wma.waterdata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LoadTimeSeriesTest {

	@MockBean
	private ObservationDao observationDao;
	@MockBean
	private TransformDao transformDao;

	private LoadTimeSeries loadTimeSeries;
	private RequestObject request;
	private List<TimeSeries> genericTimeSeriesList;
	private TimeSeries genericTimeSeries1;
	private TimeSeries genericTimeSeries2;

	@BeforeEach
	public void setupLoadTimeSeries() {
		loadTimeSeries = new LoadTimeSeries(transformDao, observationDao);
		request = new RequestObject();
		request.setUniqueId(BaseTestDao.TS_UNIQUE_ID);
		genericTimeSeriesList = new ArrayList<>();
		genericTimeSeries1 = new TimeSeries();
		genericTimeSeries2 = new TimeSeries();
	}

	// tests for fails
	@Test
	public void testNullId() {
		request.setUniqueId(null);
		ResultObject result = loadTimeSeries.processRequest(request);

		assertNotNull(result);
		assertEquals(LoadTimeSeries.STATUS_FAIL, result.getStatus());
		assertEquals(LoadTimeSeries.FAIL_MESSAGE_NULL_UNIQUE_ID, result.getFailMessage());
		assertEquals(null, result.getCount());
		assertThrows(RuntimeException.class, () -> {
			loadTimeSeries.apply(request);
		}, "should have thrown an exception but did not");
	}

	@Test
	public void testNoRecordsFound() {
		// no time series data found
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		ResultObject result = loadTimeSeries.processRequest(request);

		assertNotNull(result);
		assertEquals(LoadTimeSeries.STATUS_FAIL, result.getStatus());
		assertEquals(LoadTimeSeries.FAIL_MESSAGE_NO_RECORDS, result.getFailMessage());
		assertEquals(null, result.getCount());
		assertThrows(RuntimeException.class, () -> {
			loadTimeSeries.apply(request);
		}, "should have thrown an exception but did not");
	}

	@Test
	public void testFoundGenericFailedInsert() {
		genericTimeSeriesList.add(genericTimeSeries1);
		genericTimeSeriesList.add(genericTimeSeries2);
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		// delete succeeds
		when(observationDao.deleteTimeSeries(anyString())).thenReturn(2);
		// insert fails
		when(observationDao.insertTimeSeries(any())).thenReturn(0);
		ResultObject result = loadTimeSeries.processRequest(request);

		assertNotNull(result);
		assertEquals(LoadTimeSeries.STATUS_FAIL, result.getStatus());
		assertEquals(LoadTimeSeries.FAIL_MESSAGE_INSERT_FAILED, result.getFailMessage());
		assertEquals(0, result.getCount());
		// throws exception
		assertThrows(RuntimeException.class, () -> {
			loadTimeSeries.apply(request);
		}, "should have thrown an exception but did not");
	}

	// tests for successes
	@Test
	public void testFoundGeneric() {
		genericTimeSeriesList.add(genericTimeSeries1);
		genericTimeSeriesList.add(genericTimeSeries2);
		// 2 time series returned
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		// delete succeeds
		when(observationDao.deleteTimeSeries(anyString())).thenReturn(2);
		// insert succeeds
		when(observationDao.insertTimeSeries(any())).thenReturn(1);
		ResultObject result = loadTimeSeries.apply(request);

		assertNotNull(result);
		assertEquals(genericTimeSeriesList.size(), result.getCount());
		assertEquals(LoadTimeSeries.STATUS_SUCCESS, result.getStatus());
		assertEquals(null, result.getFailMessage());
	}

	@Test
	public void testFoundGenericNewRecords() {
		genericTimeSeriesList.add(genericTimeSeries1);
		genericTimeSeriesList.add(genericTimeSeries2);
		// happy path - 2 time series returned
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		// nothing to delete
		when(observationDao.deleteTimeSeries(anyString())).thenReturn(0);
		// insert succeeds
		when(observationDao.insertTimeSeries(any())).thenReturn(1);
		ResultObject result = loadTimeSeries.apply(request);

		assertNotNull(result);
		assertEquals(genericTimeSeriesList.size(), result.getCount());
		assertEquals(LoadTimeSeries.STATUS_SUCCESS, result.getStatus());
		assertEquals(null, result.getFailMessage());
	}
}
