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

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
public class LoadTimeSeriesTest {

	@MockBean
	private ObservationDao observationDao;
	@MockBean
	private TransformDao transformDao;
	private LoadTimeSeries loadTimeSeries;
	private RequestObject request;
	private List<TimeSeries> genericTimeSeriesList;
	TimeSeries genericTimeSeries1 = new TimeSeries();
	TimeSeries genericTimeSeries2 = new TimeSeries();

	@BeforeEach
	public void beforeEach() {
		loadTimeSeries = new LoadTimeSeries(transformDao, observationDao);
		request = new RequestObject();
		request.setUniqueId("17f83e62b06e4dc29e78d96b4426a255");
		genericTimeSeriesList = new ArrayList<>();
		genericTimeSeries1 = new TimeSeries();
		genericTimeSeries2 = new TimeSeries();
	}

	@Test
	public void testNotFound() {
		// no time series data found
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		// nothing is deleted
		when(observationDao.deleteTimeSeries(anyString())).thenReturn(0);
		// nothing is inserted
		when(observationDao.insertTimeSeries(any())).thenReturn(0);
		ResultObject result = loadTimeSeries.apply(request);
		assertNotNull(result);
		Integer expectedRowsInsertedCount = 0;
		assertEquals(expectedRowsInsertedCount, result.getCount());
		assertEquals("success", result.getStatus());
	}

	@Test
	public void testFoundGeneric() {
		genericTimeSeriesList.add(genericTimeSeries1);
		genericTimeSeriesList.add(genericTimeSeries2);
		// happy path - 2 time series returned
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		// delete succeeds
		when(observationDao.deleteTimeSeries(anyString())).thenReturn(2);
		// insert succeeds
		when(observationDao.insertTimeSeries(any())).thenReturn(1);
		ResultObject result = loadTimeSeries.apply(request);
		assertNotNull(result);
		assertEquals(genericTimeSeriesList.size(), result.getCount());
		assertEquals("success", result.getStatus());
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
		assertEquals("success", result.getStatus());
	}

	@Test
	public void testFoundGenericFailedInsert() {
		// path to the dark side
		genericTimeSeriesList.add(genericTimeSeries1);
		genericTimeSeriesList.add(genericTimeSeries2);
		when(transformDao.getTimeSeries(anyString())).thenReturn(genericTimeSeriesList);
		// delete succeeds
		when(observationDao.deleteTimeSeries(anyString())).thenReturn(2);
		// insert fails
		when(observationDao.insertTimeSeries(any())).thenReturn(0);
		ResultObject result = loadTimeSeries.apply(request);
		assertNotNull(result);
		assertNotEquals(genericTimeSeriesList.size(), result.getCount());
		assertEquals("fail", result.getStatus());
	}
}
