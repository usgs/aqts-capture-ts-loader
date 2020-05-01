package gov.usgs.wma.waterdata;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

@Component
public class ObservationDao {
	private static final Logger LOG = LoggerFactory.getLogger(ObservationDao.class);

	@Value("${PARM_REFERENCE_URL}")
	public String parmReferenceUrl;

	@Value("${STAT_REFERENCE_URL}")
	public String statReferenceUrl;

	@Autowired
	@Qualifier("jdbcTemplateObservation")
	protected JdbcTemplate jdbcTemplate;

	@Value("classpath:sql/deleteTimeSeries.sql")
	protected Resource deleteQuery;

	@Value("classpath:sql/insertTimeSeries.sql")
	protected Resource insertQuery;

	@Transactional
	public Integer deleteTimeSeries(String timeSeriesUniqueId) {
		Integer rowsDeletedCount = null;
		try {
			String sql = new String(FileCopyUtils.copyToByteArray(deleteQuery.getInputStream()));
			rowsDeletedCount = jdbcTemplate.update(
					sql,
					timeSeriesUniqueId
			);
		} catch (EmptyResultDataAccessException e) {
			LOG.info("Couldn't find {} - {} ", timeSeriesUniqueId, e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error("Unable to get SQL statement", e);
			throw new RuntimeException(e);
		}
		return rowsDeletedCount;
	}


	public int insertTimeSeries(String timeSeriesUniqueId, List<TimeSeries> timeSeries) {
		int rowsInsertedCount = 0;
		try {
			String sql = new String(FileCopyUtils.copyToByteArray(insertQuery.getInputStream()));
			int [] rowsInsertedCounts = jdbcTemplate.batchUpdate(
					sql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setString(1, timeSeries.get(i).getGroundwaterDailyValueIdentifier());
							ps.setString(2, timeSeries.get(i).getTimeSeriesUniqueId());
							ps.setString(3, timeSeries.get(i).getMonitoringLocationIdentifier());
							ps.setString(4, timeSeries.get(i).getMonitoringLocationIdentifier());
							ps.setString(5, timeSeries.get(i).getObservedPropertyId());
							ps.setString(6, timeSeries.get(i).getObservedPropertyId());
							ps.setString(7, String.format(parmReferenceUrl, timeSeries.get(i).getObservedPropertyId()));
							ps.setString(8, timeSeries.get(i).getStatisticId());
							ps.setString(9, timeSeries.get(i).getStatisticId());
							ps.setString(10, String.format(statReferenceUrl, timeSeries.get(i).getStatisticId()));
							ps.setDate(11, timeSeries.get(i).getTimeStep());
							ps.setString(12, timeSeries.get(i).getUnitOfMeasure());
							ps.setString(13, timeSeries.get(i).getResult());
							ps.setString(14, timeSeries.get(i).getApprovals());
							ps.setString(15, timeSeries.get(i).getQualifiers());
							ps.setString(16, timeSeries.get(i).getGrades());
						}
						@Override
						public int getBatchSize() {
							return timeSeries.size();
						}
					}
			);
			rowsInsertedCount = Arrays.stream(rowsInsertedCounts).sum();
		} catch (EmptyResultDataAccessException e) {
			LOG.info("Couldn't find {} - {} ", timeSeries.get(0).getTimeSeriesUniqueId(), e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error("Unable to get SQL statement", e);
			throw new RuntimeException(e);
		}
		return rowsInsertedCount;
	}
}
