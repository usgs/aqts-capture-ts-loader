package gov.usgs.wma.waterdata;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadTimeSeries implements Function<RequestObject, ResultObject> {

	private static final Logger LOG = LoggerFactory.getLogger(LoadTimeSeries.class);

	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAIL = "fail";

	private TransformDao transformDao;
	private ObservationDao observationDao;

	@Autowired
	public LoadTimeSeries(TransformDao transformDao, ObservationDao observationDao) {
		this.transformDao = transformDao;
		this.observationDao = observationDao;
	}

	@Override
	public  ResultObject apply(RequestObject request) {
		return processRequest(request);
	}

	protected ResultObject processRequest(RequestObject request) {

		String timeSeriesUniqueId = request.getUniqueId();
		ResultObject result = new ResultObject();

		if (null != timeSeriesUniqueId) {

			// get time series from transform db
			List<TimeSeries> timeSeries = transformDao.getTimeSeries(timeSeriesUniqueId);

			// delete existing time series from observation db
			observationDao.deleteTimeSeries(timeSeriesUniqueId);

			Integer count = 0;
			for (TimeSeries ts : timeSeries) {
				// insert time series into observation db
				count += observationDao.insertTimeSeries(ts);
			}
			result.setCount(count);

			if (count == timeSeries.size() && count != 0) {
				result.setStatus(STATUS_SUCCESS);
				LOG.debug("Successfully inserted time series with unique id: {} ", timeSeriesUniqueId);
			} else {
				result.setStatus(STATUS_FAIL);
				LOG.debug("Selected row count: {} and inserted row count: {} differ or no records were found.", timeSeries.size(), count);
			}
		} else {
			result.setStatus(STATUS_FAIL);
		}
		return result;
	}
}