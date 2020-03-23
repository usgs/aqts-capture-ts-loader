package gov.usgs.wma.waterdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


	public Integer insertTimeSeries(TimeSeries timeSeries) {
		Integer rowsInsertedCount = null;
		try {
			String sql = new String(FileCopyUtils.copyToByteArray(insertQuery.getInputStream()));
			rowsInsertedCount = jdbcTemplate.update(
					sql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, timeSeries.getGroundwaterDailyValueIdentifier());
							ps.setString(2, timeSeries.getTimeSeriesUniqueId());
							ps.setString(3, timeSeries.getMonitoringLocationIdentifier());
							ps.setString(4, timeSeries.getMonitoringLocationIdentifier());
							ps.setString(5, timeSeries.getObservedPropertyId());
							ps.setString(6, timeSeries.getObservedPropertyId());
							ps.setString(7, String.format(parmReferenceUrl, timeSeries.getObservedPropertyId()));
							ps.setString(8, timeSeries.getStatisticId());
							ps.setString(9, timeSeries.getStatisticId());
							ps.setString(10, String.format(statReferenceUrl, timeSeries.getStatisticId()));
							ps.setDate(11, timeSeries.getTimeStep());
							ps.setString(12, timeSeries.getUnitOfMeasure());
							ps.setString(13, timeSeries.getResult());
							ps.setString(14, timeSeries.getApprovals());
							ps.setString(15, timeSeries.getQualifiers());
							ps.setString(16, timeSeries.getGrades());
						}
					}
			);
		} catch (EmptyResultDataAccessException e) {
			LOG.info("Couldn't find {} - {} ", timeSeries.getTimeSeriesUniqueId(), e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error("Unable to get SQL statement", e);
			throw new RuntimeException(e);
		}
		return rowsInsertedCount;
	}
}
